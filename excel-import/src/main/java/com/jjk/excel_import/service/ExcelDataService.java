package com.jjk.excel_import.service;

import java.util.List;

public interface ExcelDataService {
	public List<Object[]> readExcelFile(String path);
	boolean insertIntoDatabase(List<Object[]> objList);
}
