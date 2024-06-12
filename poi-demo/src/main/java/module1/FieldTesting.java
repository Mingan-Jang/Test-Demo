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

import lombok.Data;

public class FieldTesting {
	Integer a = 123;
	int b = 3;

	public static void main(String[] args) {
		FieldTesting fieldTesting = new FieldTesting();

		try {
			Field fieldA = fieldTesting.getClass().getDeclaredField("a");
			fieldA.setAccessible(true);

			// 錯誤：預期是 int 而非 Object
//			int intValueA = fieldA.getInt(fieldTesting);
//			Integer valueA = intValueA;
			Integer valueA2 = (Integer) fieldA.get(fieldTesting);
			System.out.println("a = " + valueA2);
			System.out.println(fieldA.getType());
			// 获取字段 b
			Field fieldB = fieldTesting.getClass().getDeclaredField("b");
			// 获取字段 b 的值
			int valueB = fieldB.getInt(fieldTesting);
			System.out.println("b = " + valueB);
			System.out.println(fieldB.getType());

		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
