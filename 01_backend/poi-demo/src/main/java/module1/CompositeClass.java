package module1;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dto.CompositeDTO;
import dto.ExtendDTO;
import dto.PrimaryDTO;
import mapper.CompositeMapper;

public class CompositeClass {
	public static void main(String[] args) {
		String excelPath = "excel/composite.xlsx";
		Integer firstRow = 1;
		Map<String, String> columnMapping = new HashMap<>();
		columnMapping.put("姓名", "name");
		columnMapping.put("年齡", "age");
		columnMapping.put("郵箱", "email");
		columnMapping.put("地址", "address");

		Set<String> aFields = getFieldNames(PrimaryDTO.class);
		Set<String> bFields = getFieldNames(ExtendDTO.class);

		try (FileInputStream fis = new FileInputStream(excelPath); Workbook wb = new XSSFWorkbook(fis)) {
			Sheet sht = wb.getSheetAt(0);

			Row headerRow = sht.getRow(firstRow);
			Map<String, Integer> columnIndexMap = new HashMap<>();
			for (Cell cell : headerRow) {
				String columnName = cell.getStringCellValue();
				columnIndexMap.put(columnMapping.get(columnName), cell.getColumnIndex());
			}

			for (int i = firstRow + 1; i <= sht.getLastRowNum(); i++) {
				Row row = sht.getRow(i);
				if (row == null) {
					continue;
				}

				PrimaryDTO primaryDTO = new PrimaryDTO();
				ExtendDTO extendDTO = new ExtendDTO();

				for (Entry<String, Integer> entry : columnIndexMap.entrySet()) {
					String fieldName = entry.getKey();
					Integer columnIndex = entry.getValue();
					Cell cell = row.getCell(columnIndex);
					String value = getCellValue(cell);

					if (aFields.contains(fieldName)) {
						setFieldValue(primaryDTO, fieldName, value);
					} else if (bFields.contains(fieldName)) {
						setFieldValue(extendDTO, fieldName, value);
					}
				}
				CompositeDTO composite = CompositeMapper.INSTANCE.toCompositeDTO(extendDTO, primaryDTO);
				
				System.out.println(composite);
				primaryDTO = null;
				extendDTO = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Set<String> getFieldNames(Class<?> clazz) {
		Set<String> fieldNames = new HashSet<>();
		for (Field field : clazz.getDeclaredFields()) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	private static void setFieldValue(Object obj, String fieldName, String value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);

			if (field.getType() == int.class) {
	            int temp = (int) Double.parseDouble(value);
				field.setInt(obj,temp);
			} else {
				field.set(obj, value);
			}
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private static String getCellValue(Cell cell) {
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

}
