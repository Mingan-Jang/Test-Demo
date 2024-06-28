package com.jjk.upload_test.controller;

import java.io.IOException;
import java.net.http.HttpRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.jjk.upload_test.dto.ImportDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/uploads")
public class UploadFileAndDtoController {

	// https://stackoverflow.com/questions/63367971/openapi-send-multipartfile-request-with-json-get-application-octet-stream-er
	@Operation(summary = "上传文件和 DTO 控制器")
	@PostMapping(value = "/FileDtoUploadMixed2", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> uploadMixed(
			@RequestPart(value = "importDTO") ImportDTO importDTO,
			@RequestPart(value ="images" ,required = false) MultipartFile[] files , HttpServletRequest req) throws IOException {
		
		System.out.println(req.getContentType());
		return ResponseEntity.ok("success");
	}


//	
//	@Operation(summary = "上传文件和 DTO 控制器")
//	@PostMapping(value = "/FileDtoUploadOctstream", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	public ResponseEntity<?> uploadOctstream(@RequestPart(value="importDTO") ImportDTO importDTO,
//			@RequestPart("images") MultipartFile[] files) throws IOException {
//		
//		return ResponseEntity.ok("success");
//	}
//	
//	
//	
//	@Operation(summary = "上传文件和 DTO 控制器")
//	@PostMapping(value = "/FileDtoUploadFormDataReqPart", consumes = "multipart/form-data")
//	public ResponseEntity<?> uploadFormData(@RequestPart(value="importDTO") ImportDTO importDTO,
//			@RequestPart("images") MultipartFile[] files) throws IOException {
//		
//		return ResponseEntity.ok("success");
//	}
//	
//	
//	@Operation(summary = "上传文件和 DTO 控制器")
//	@PostMapping(value = "/FileDtoUpload2", consumes = "multipart/form-data")
//	public ResponseEntity<?> uploadImage2(@RequestPart ImportDTO importDTO,
//			@RequestPart("images") MultipartFile[] files) throws IOException {
//		
//		return ResponseEntity.ok("success");
//	}

}
