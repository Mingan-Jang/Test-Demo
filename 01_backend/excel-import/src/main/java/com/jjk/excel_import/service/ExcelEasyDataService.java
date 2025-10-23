package com.jjk.excel_import.service;

public interface ExcelEasyDataService {
	public boolean readExcelFile(String path);
	public boolean insertIntoDatabase();
}
