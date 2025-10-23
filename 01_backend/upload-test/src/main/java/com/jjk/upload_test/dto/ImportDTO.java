package com.jjk.upload_test.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class ImportDTO {
	
	@Schema(example = "Yes")
	String name;
	
	@Schema(example = "132")
	String age;
}
