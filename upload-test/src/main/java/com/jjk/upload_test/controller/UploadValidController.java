package com.jjk.upload_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jjk.upload_test.dto.RequestDTO;
import com.jjk.upload_test.dto.RequestDateDTO;
import com.jjk.upload_test.dto.ResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/valid")
public class UploadValidController {

	
	// Valid 必須加上dependency 才會真的有效
	// 
	@Operation(summary = "Put method @RequestBody , return with ResponseEntity of a RequestDTO object")
	@PutMapping("/validRequestBodyReturnResponseEntity")
    @ResponseBody
	public ResponseEntity<?> putpathRequestBodyReturnResponseEntity(@Valid @RequestBody RequestDateDTO requestDateDTO) {
		System.out.println(requestDateDTO.getName());
		System.out.println(requestDateDTO.getAge());
		
		System.out.println("getDateInJSONFormatGMT08 = " +requestDateDTO.getDateInJSONFormatGMT08());

		System.out.println("getDateInJSONFormatGMT02 = " +requestDateDTO.getDateInJSONFormatGMT02());

		System.out.println("getDateInJSONFormatInDEFAULT_TIMEZONE = " + requestDateDTO.getDateInJSONFormatInDEFAULT_TIMEZONE());


		System.out.println("getDateInJSONFormat = " + requestDateDTO.getDateInJSONFormat());
		
		System.out.println("getDateInDefaultFormat = " +requestDateDTO.getDateInDefaultFormat());
		
		System.out.println(requestDateDTO.getDateSSX());
		System.out.println(requestDateDTO.getDateSSZ());
		System.out.println(requestDateDTO.getDateTimeFormatDate());
		System.out.println(requestDateDTO.getDateTimeFormatDateTime());
		System.out.println(requestDateDTO.getDateTimeFormatNone());
		System.out.println(requestDateDTO.getDateTimeFormatTime());

		
		return ResponseEntity.ok(requestDateDTO);
	}

}
