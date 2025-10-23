package com.jjk.env_test.maintest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToCsvConverter {

	public static void main(String[] args) throws IOException {
		// Load JSON data
		ObjectMapper mapper = new ObjectMapper();
		JsonNode items = mapper.readTree(new File("C:\\Users\\2405015\\Desktop\\20241111_屏榮資料\\All.json")).get("items");

		String[] headers = { "ordept", "roomandbed", "orhisnum", "patientName", "orproced", "orreqno", "chkDateTime",
				"oroedt", "ordocnm", "orstatusname", "detailInfo" };

		FileWriter csvWriter = new FileWriter("C:\\Users\\2405015\\Desktop\\20241111_屏榮資料\\output.csv");
		csvWriter.append(String.join(",", headers)).append("\n");

		for (JsonNode item : items) {
			List<String> row = new ArrayList<>();

			// Extract fields based on headers
			row.add(getTextValue(item, "ordept"));
			row.add(getTextValue(item, "roomandbed"));
			row.add(getTextValue(item, "orhisnum"));
			row.add(getTextValue(item, "namec")); // "namec" is mapped to "patientName"
			row.add(getTextValue(item, "orproced"));
			row.add(getTextValue(item, "orreqno"));

			// Combine "shdate" and "chktime" for "chkDateTime"
			String chkDateTime = getTextValue(item, "shdate") + " " + getTextValue(item, "chktime");
			row.add(chkDateTime.trim());

			row.add(getTextValue(item, "oroedt")); // "oroedt" for opening date
			row.add(getTextValue(item, "ordocnm")); // "ordocnm" for doctor name
			row.add(getTextValue(item, "orstatusname"));

			// Build "detailInfo" using buildNote method
			row.add(buildNote(item));

			// Join row and write to CSV
			csvWriter.append(String.join(",", row)).append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
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