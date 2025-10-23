package com.jjk.excel_import.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.jjk.excel_import.dto.EventDTO;
import com.jjk.excel_import.dto.ExtendDTO;
import com.jjk.excel_import.enumer.Err;
import com.jjk.excel_import.exception.RestOutException;
import com.jjk.excel_import.service.ExcelEasyDataService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelEasyDataServiceImpl implements ExcelEasyDataService {
	private final String regexp = "^臺鐵月報表_\\d{8}_\\d{6}\\.xlsx$";

	@Value("${mapping.prop}")
	private String mappingString;

	@Value("${report.setting.firstrow}")
	private Integer firstRow;

	@Override
	public boolean readExcelFile(String path) {

		if (path.contains(".xlsx")) {
			// 讀取Excel
			int lastIndex = path.lastIndexOf("/");
			String filename = path.substring(lastIndex + 1);
			if (!filename.matches(regexp)) {
				throw new RestOutException(Err.BAD_REQUEST.getCode(), "檔案名稱不符" + regexp);
			}
		} 

		return false;
	}

	@Override
	public boolean insertIntoDatabase() {
		return false;
	}

	public List<?> readByFile(String path, Map<String, VarInfo> columnMapping, Class<?>... dtoClasses) {
		try (FileInputStream fis = new FileInputStream(path); Workbook wb = new XSSFWorkbook(fis)) {
			// 掃描Header Row，獲取欄位對應的column
			Sheet sht = wb.getSheetAt(0);
			Row headerRow = sht.getRow(firstRow);
			Map<Integer, ColumnInfo> columnInfoMap = new HashMap<>();

			for (Cell cell : headerRow) {
				String zwVarStr = cell.getStringCellValue();
				if (Objects.equals(null, zwVarStr)) {
					throw new RestOutException(Err.INTERNAL_ERROR.getCode(), "試算表格式有錯，首Row不得為空");
				}

				VarInfo varInfo = columnMapping.get(zwVarStr);

				if (varInfo == null) {
					log.info("Unable to map the " + varInfo + ", please check mapping properties file");
				}
				ColumnInfo columnInfo = new ColumnInfo(varInfo.getClassName(), varInfo.getEngVarStr(),
						cell.getColumnIndex());
//				columnInfoList.add(columnInfo);
				columnInfoMap.put(cell.getColumnIndex(), columnInfo);

			}

			for (int i = firstRow + 1; i <= sht.getLastRowNum(); i++) {
				Row row = sht.getRow(i);
				if (row == null) {
					continue;
				}

				EventDTO eventDTO = new EventDTO();
				ExtendDTO extendDTO = new ExtendDTO();

				for (Cell cell : row) {
					ColumnInfo columnInfo = columnInfoMap.get(cell.getColumnIndex());
					String className = columnInfo.getClassName();
					String engStr = columnInfo.getEngVarStr();
					
					String currentId = 
					if ("EventDTO".equals(className)) {
						setField(eventDTO, engStr, getCellString(cell));
						eventDTO.setOid(path);
					} else if ("ExtendDTO".equals(className)) {
						setField(extendDTO, engStr, getCellString(cell));
					}

				}

				eventDTO = null;
				extendDTO = null;
			}

		} catch (IOException e) {
			throw new RestOutException(Err.INTERNAL_ERROR.getCode(), "試算表讀取失敗");
		}
		return null;
	}

	private Map<String, VarInfo> loadColumnMappings(String mappingString) {
		Properties properties = new Properties();
		Resource mappingResource = new ClassPathResource(mappingString);
		Map<String, VarInfo> columnMapping = new HashMap<>();
		try (InputStream inputStream = mappingResource.getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			properties.load(reader);
			properties.forEach((key, value) -> {
				String columnEngName = (String) key;
				String varInfoStr = (String) value;
				String[] splitValue = varInfoStr.split("\\.");
				VarInfo varInfo = new VarInfo();
				varInfo.setClassName(splitValue[0]);
				varInfo.setEngVarStr(splitValue[1]);

				columnMapping.put(columnEngName, varInfo);
			});
		} catch (IOException e) {
			throw new RestOutException(Err.BAD_REQUEST.getCode(), "確認中英mapping表(預設為column_mapping.properties)是否存在");
		}

		return columnMapping;
	}

	@Data
	private class VarInfo {
		private String className;
		private String engVarStr;
	}

	private String getCellString(Cell cell) {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getLocalDateTimeCellValue().toString();
			} else {
				return Double.toString(cell.getNumericCellValue());
			}
		case BOOLEAN:
			return Boolean.toString(cell.getBooleanCellValue());
		case FORMULA:
			return cell.getCellFormula();
		default:
			return "";

		}
	}

	@Data
	@AllArgsConstructor
	private class ColumnInfo {
		private String className;
		private String engVarStr;
		private int columnIndex;
	}

	private static void setField(Object obj, String fieldName, String value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);

			if (field.getType() == int.class) {
				int temp = (int) Double.parseDouble(value);
				field.setInt(obj, temp);
			} else {
				field.set(obj, value);
			}
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private String generateEventOid() {
		return UUID.randomUUID().toString();
	}
}

