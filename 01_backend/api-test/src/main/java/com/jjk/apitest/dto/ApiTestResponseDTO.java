package com.jjk.apitest.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
public class ApiTestResponseDTO {
	String data;
	
    @JsonFormat(pattern="yyyy-MM")
	Date date = new Date();
	String status;
}
