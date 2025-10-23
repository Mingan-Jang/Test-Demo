package com.jjk.upload_test.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RequestDTO {
	@Size(min=3)
	String name;
	String age;
}
