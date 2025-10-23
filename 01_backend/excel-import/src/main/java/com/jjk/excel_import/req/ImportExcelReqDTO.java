package com.jjk.excel_import.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="引入Excel所需條件")
public class ImportExcelReqDTO {
	@Schema(description = "excel路徑",example="excel/臺鐵月報表_20240999_999999.xlsx")
	private String path;
}
