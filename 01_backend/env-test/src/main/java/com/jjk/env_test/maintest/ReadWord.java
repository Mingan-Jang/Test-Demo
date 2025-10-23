package com.jjk.env_test.maintest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class ReadWord {
	public static void main(String[] args) {

		List<String> classifier = Arrays.asList("一般行車事故", "重大行車事故", "異常事件");
		Map<String, String> KEYWORD_MAP = new HashMap<>();

		List<String> categoryTypes = new ArrayList<String>();

		Optional<String> operatorOpt = Optional.empty();

		String filePath = "C:\\Users\\2405015\\Desktop\\20241029_鐵道運意見\\測試.docx";
		try (FileInputStream fis = new FileInputStream(filePath); XWPFDocument document = new XWPFDocument(fis)) {

			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				if (!operatorOpt.isPresent()) {
					// # 如果沒有operator需要找到operator
					operatorOpt = KEYWORD_MAP.entrySet().stream()
							.filter(entry -> paragraph.getText().contains(entry.getKey())).map(Map.Entry::getValue)
							.findFirst();
				}

				// # 找尋事故分類classifier
				Optional<String> foundClassifier = classifier.stream().filter(paragraph.getText().trim()::contains)
						.findFirst();

				foundClassifier.ifPresent(categoryTypes::add);

				if (foundClassifier.isEmpty() && !paragraph.getText().equals("")) {
					System.out.println("No matching classifier found in paragraph: " + paragraph.getText());
				}

			}

			List<XWPFTable> tables = document.getTables();
			for (XWPFTable table : tables) {
				for (XWPFTableRow row : table.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						System.out.print(cell.getText() + "\t"); // 用制表符分隔单元格内容
					}
					System.out.println(); // 换行以分隔行
				}
				System.out.println(); // 分隔不同表格
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
