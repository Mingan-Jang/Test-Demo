package com.jjk.excel_import.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResGeneralDTO {
	private Integer errcode;
	private String errorMessage;
	private Object data;
}
