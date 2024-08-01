package com.jjk.spring_data.dto;

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
	
	@Schema(example = "132")
	String job;
}
