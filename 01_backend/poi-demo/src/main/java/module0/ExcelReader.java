package module0;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelReader {
    public static void main(String[] args) {
        String excelFilePath = "excel/excel-file.xlsx";

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 读取第一个工作表
            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                        	System.out.println("STRING");
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                        	System.out.println("NUMERIC");
                            if (DateUtil.isCellDateFormatted(cell)) {
                            	System.out.println("DateFormat");
                                System.out.print(cell.getDateCellValue() + "\t");
                            } else {
                            	System.out.println("NumericCellValue");
                                System.out.print(cell.getNumericCellValue() + "\t");
                            }
                            break;
                        case BOOLEAN:
                        	System.out.println("BOOLEAN");
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        case FORMULA:
                        	System.out.println("FORMULA");
                            System.out.print(cell.getCellFormula() + "\t");
                            break;
                        default:
                            System.out.print("UNKNOWN\t");
                            break;
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}