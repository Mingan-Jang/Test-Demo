package com.jjk.env_test.maintest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class TikaTest {

	public static void main(String[] args) {
		// 替換為你的檔案路徑
		String docxFilePath = "C:\\Users\\2405015\\Desktop\\20241029_鐵道運意見\\測試.docx";
		String xlsxFilePath = "C:\\Users\\2405015\\Desktop\\20241029_鐵道運意見\\高鐵月報表_20240529_170149.xlsx";

		// 測試 docx 檔案
		testFile(docxFilePath, "docx");

		// 測試 xlsx 檔案
		testFile(xlsxFilePath, "xlsx");
	}

	private static void testFile(String filePath, String extension) {
		try {
			// 使用檔案路徑創建 File 對象
			File file = new File(filePath);
			byte[] fileContent = readFileContent(file); // 读取文件内容为字节数组

			// 创建 DTO
			FileUploadDTO fileUploadDTO = new FileUploadDTO(file.getName(), extension, fileContent);

			// 校驗文件類型
			boolean isValid = checkFileType(fileUploadDTO);
			System.out.println(filePath + " 檔案為 " + extension + " 格式檢測結果: " + isValid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte[] readFileContent(File file) throws IOException {
		try (InputStream inputStream = new FileInputStream(file)) {
			return inputStream.readAllBytes(); // 读取文件内容为字节数组
		}
	}

	public static boolean checkFileType(FileUploadDTO fileUploadDTO) {
		switch (fileUploadDTO.getExtension().toLowerCase()) {
		case "docx": {
			try (InputStream inputStream = new ByteArrayInputStream(fileUploadDTO.getFileContent())) {
				new XWPFDocument(inputStream).close();
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		case "xlsx": {
			try (InputStream inputStream = new ByteArrayInputStream(fileUploadDTO.getFileContent())) {
				new XSSFWorkbook(inputStream).close();
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		default:
			throw new RuntimeException("不支援的驗證類型: " + fileUploadDTO.getExtension());
		}
	}

	// DTO类
	public static class FileUploadDTO {
		private String fileName;
		private String extension;
		private byte[] fileContent;

		public FileUploadDTO(String fileName, String extension, byte[] fileContent) {
			this.fileName = fileName;
			this.extension = extension;
			this.fileContent = fileContent;
		}

		public String getFileName() {
			return fileName;
		}

		public String getExtension() {
			return extension;
		}

		public byte[] getFileContent() {
			return fileContent;
		}
	}
}
