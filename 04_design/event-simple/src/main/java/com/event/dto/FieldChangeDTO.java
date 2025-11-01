package com.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "欄位變更資訊")
public class FieldChangeDTO {
	@Schema(description = "欄位名稱", example = "priority")
	private String fieldName;

	@Schema(description = "舊值", example = "LOW")
	private Object oldValue;

	@Schema(description = "新值", example = "HIGH")
	private Object newValue;

	private String className;
}