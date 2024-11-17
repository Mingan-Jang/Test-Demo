package com.jjk.env_test.maintest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToXlsxConverter {

	public static void main(String[] args) throws IOException {
		// Load JSON data
		ObjectMapper mapper = new ObjectMapper();
		JsonNode items = mapper.readTree(new File("C:\\Users\\2405015\\Desktop\\20241111_屏榮資料\\All.json")).get("items");

		String[] headers = { "ordept", "roomandbed", "orhisnum", "patientName", "orproced", "orreqno", "chkDateTime",
				"oroedt", "ordocnm", "orstatusname", "detailInfo" };

		// Create workbook and sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Data");

		// Write headers to the first row
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Write data rows
		int rowNum = 1;
		for (JsonNode item : items) {
			Row row = sheet.createRow(rowNum++);

			// Extract fields based on headers
			row.createCell(0).setCellValue(getTextValue(item, "ordept"));
			row.createCell(1).setCellValue(getTextValue(item, "roomandbed"));
			row.createCell(2).setCellValue(getTextValue(item, "orhisnum"));
			row.createCell(3).setCellValue(getTextValue(item, "namec")); // "namec" is mapped to "patientName"
			row.createCell(4).setCellValue(getTextValue(item, "orproced"));
			row.createCell(5).setCellValue(getTextValue(item, "orreqno"));

			// Combine "shdate" and "chktime" for "chkDateTime"
			String chkDateTime = getTextValue(item, "shdate") + " " + getTextValue(item, "chktime");
			row.createCell(6).setCellValue(chkDateTime.trim());

			row.createCell(7).setCellValue(getTextValue(item, "oroedt")); // "oroedt" for opening date
			row.createCell(8).setCellValue(getTextValue(item, "ordocnm")); // "ordocnm" for doctor name
			row.createCell(9).setCellValue(getTextValue(item, "orstatusname"));

			// Build "detailInfo" using buildNote method
			row.createCell(10).setCellValue(buildNote(item));
		}

		// Write to XLSX file
		FileOutputStream fileOut = new FileOutputStream("C:\\Users\\2405015\\Desktop\\20241111_屏榮資料\\output.xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Close the workbook
		workbook.close();
	}

	private static String getTextValue(JsonNode item, String fieldName) {
		return item.hasNonNull(fieldName) ? item.get(fieldName).asText() : "";
	}

	private static String buildNote(JsonNode item) {
		StringBuilder detailInfo = new StringBuilder();
		String schTime = "";
		String signTime = "";

		// Construct scheduling information
		if (item.has("isactive") && "N".equals(item.get("isactive").asText())) {
			schTime += "原排程日期: " + getTextValue(item, "shdate") + " " + getTextValue(item, "chktime") + " 原因: "
					+ getTextValue(item, "cancelname");
		}

		// Construct signature time information
		if (item.hasNonNull("orrcpdt")) {
			signTime += "已簽收: " + getTextValue(item, "orrcpdt") + " " + getTextValue(item, "orrcptm") + " "
					+ getTextValue(item, "orrcpnm");
		}

		// Append details
		String orfreqnname = getTextValue(item, "orfreqnname");
		if (!orfreqnname.isEmpty()) {
			detailInfo.append(orfreqnname);
		}
		if (!schTime.isEmpty()) {
			if (detailInfo.length() > 0)
				detailInfo.append("; ");
			detailInfo.append(schTime);
		}
		if (!signTime.isEmpty()) {
			if (detailInfo.length() > 0)
				detailInfo.append("; ");
			detailInfo.append(signTime);
		}

		return detailInfo.toString();
	}
}
