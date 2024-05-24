package com.playground.playground.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MultipartDTO {
	String name;
	MultipartFile file;
}
