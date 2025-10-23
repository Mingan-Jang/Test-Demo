package com.jjk.excel_import.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
	private String oid;
	private String operator;
	private String occurTime;

	// Read with RSM event
	// ExcelDataDTO.categoryName --> categoryId
	private String categoryId;
	private String categoryName;

	// Read with RSM event
	// ExcelDataDTO.categoryName --> categoryType
//	private String categoryType;

	private String occurDesc;
	
	
}
