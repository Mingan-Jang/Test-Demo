package com.jjk.excel_import.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.jjk.excel_import.dto.EventDTO;
import com.jjk.excel_import.dto.ExtendDTO;
import com.jjk.excel_import.enumer.Err;
import com.jjk.excel_import.exception.RestOutException;
import com.jjk.excel_import.mapper.ExcelMapper;
import com.jjk.excel_import.repo.RepositoryManager;
import com.jjk.excel_import.service.ExcelDataService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExcelDataServiceImpl implements ExcelDataService {
	private final String regexp = "^臺鐵月報表_\\d{8}_\\d{6}\\.xlsx$";

	@Autowired
	RepositoryManager repositoryManager;

	@Value("${mapping.prop}")
	private String mappingString;

	@Value("${report.setting.firstrow}")
	private Integer firstRow;

//	@Value("classpath:column_mapping.properties")
//	private Resource mappingResource;

	@Override
	public List<Object[]> readExcelFile(String path) {

		if (path.contains(".xlsx")) {
			// 讀取Properties
			Map<String, VarInfo> columnMapping = loadColumnMappings(mappingString);

			// 讀取Excel
			int lastIndex = path.lastIndexOf("/");
			String filename = path.substring(lastIndex + 1);
			if (!filename.matches(regexp)) {
				throw new RestOutException(Err.BAD_REQUEST.getCode(), "檔案名稱不符" + regexp);
			}

			List<Object[]> objList = readByFile(path, columnMapping, EventDTO.class, ExtendDTO.class);
//			for (Object[] obj : objList) {
//				for (int i = 0; i < obj.length; i++) {
//					System.out.println(obj[i].toString());
//				}
//			} 

			return objList;

		} else {
			// 資料夾搜尋所有檔案
		}

		return null;
	}

	@Override
	public boolean insertIntoDatabase(List<Object[]> objList) {
		for (Object[] obj : objList) {
			for (Object po : obj) {
				repositoryManager.saveToDatabase(po);
			}
		}
		return true;
	}

	public List<Object[]> readByFile(String path, Map<String, VarInfo> columnMapping, Class<?>... dtoClasses) {
		List<Object[]> objList = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(path); Workbook wb = new XSSFWorkbook(fis)) {
			Sheet sht = wb.getSheetAt(0);
			Row headerRow = sht.getRow(firstRow);
			Map<Integer, ColumnInfo> columnInfoMap = new HashMap<>();

			for (Cell cell : headerRow) {
				String zwVarStr = getCellString(cell);
				if (Objects.equals(null, zwVarStr)) {
					throw new RestOutException(Err.INTERNAL_ERROR.getCode(), "試算表格式有錯，首Row不得為空");
				}

				VarInfo varInfo = columnMapping.get(zwVarStr);

				if (varInfo == null) {
					log.warn("Unable to map the " + zwVarStr + ", please check mapping properties file");
					continue;
				}
				ColumnInfo columnInfo = new ColumnInfo(varInfo.getClassName(), varInfo.getEngVarStr(),
						cell.getColumnIndex());
				columnInfoMap.put(cell.getColumnIndex(), columnInfo);
			}

			for (int i = firstRow + 1; i <= sht.getLastRowNum(); i++) {
				Row row = sht.getRow(i);
				if (row == null) {
					continue;
				}

				Object[] dtoObjects = new Object[dtoClasses.length];
				for (int j = 0; j < dtoClasses.length; j++) {
					Object dtoObject = dtoClasses[j].getDeclaredConstructor().newInstance();

					for (Map.Entry<Integer, ColumnInfo> entry : columnInfoMap.entrySet()) {
						int columnIndex = entry.getKey();
						ColumnInfo columnInfo = entry.getValue();
						Cell cell = row.getCell(columnIndex);
						if (cell == null) {
							continue;
						}

						String className = columnInfo.getClassName();
						String engStr = columnInfo.getEngVarStr();

						if (dtoClasses[j].getSimpleName().equals(className)) {
							setField(dtoObject, engStr, getCellString(cell));
						}
					}

					dtoObjects[j] = dtoObject;
				}
				// 將 DTO 物件轉換成 PO 物件
				Object[] poObjects = new Object[dtoObjects.length];
				for (int k = 0; k < dtoObjects.length; k++) {
					poObjects[k] = convertDtoToPo(dtoObjects[k]);
				}

				objList.add(poObjects);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// 处理实例化异常
			e.printStackTrace();
		} catch (IOException e) {
			// 处理文件读取异常
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return objList;
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

	private Object convertDtoToPo(Object dto) {
		try {
			String dtoClassName = dto.getClass().getSimpleName();
			String poClassName = dtoClassName.replace("DTO", "PO");
			String mapperMethodName = dtoClassName.substring(0, 1).toLowerCase() + dtoClassName.substring(1) + "To"
					+ poClassName;

			Method mapperMethod = ExcelMapper.class.getMethod(mapperMethodName, dto.getClass());
			return mapperMethod.invoke(ExcelMapper.INSTANCE, dto);
		} catch (Exception e) {
			throw new RuntimeException("DTO to PO conversion failed", e);
		}
	}

	private String generateEventOid() {
		return UUID.randomUUID().toString();
	}

}
