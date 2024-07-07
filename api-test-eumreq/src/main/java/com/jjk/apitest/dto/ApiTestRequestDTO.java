package com.jjk.apitest.dto;

import com.jjk.apitest.enumeration.FileActionEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiTestRequestDTO {
	
	@Schema(example="aaa")
	String name;
	
	@Schema(example="admin")
	String role;
	
	@Schema(example="UPLOAD_FILE")
	FileActionEnum fileActionEnum; 
}
