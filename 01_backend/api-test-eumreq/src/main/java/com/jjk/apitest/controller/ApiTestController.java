package com.jjk.apitest.controller;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.jjk.apitest.dto.ApiTestRequestDTO;
import com.jjk.apitest.exception.RestException;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Enum req dto  testeing")
@RestController
@RequestMapping("/api")
public class ApiTestController {
	@PostMapping("/enum")
	public ResponseEntity<?> enumReqDtoTesting(@RequestBody ApiTestRequestDTO req) {
		System.out.println(req.getFileActionEnum());
		return ResponseEntity.ok("success");
	}

	@PostMapping("/enum/restException")
	public ResponseEntity<?> enumReqDtoRestException(@RequestBody ApiTestRequestDTO req) throws Exception {
		String msg = "Local enumReqDtoRestException thrown";
		System.out.println(msg);
		throw new RestException("msg");
	}

	@PostMapping("/enum/notReadableException")
	public ResponseEntity<?> enumReqDtoHttpMessageNotReadableException(@RequestBody ApiTestRequestDTO req)
			throws Exception {
		String msg = "Local HttpMessageNotReadableException thrown";
		System.out.println(msg);
		HttpInputMessage httpMsg = null;
		throw new HttpMessageNotReadableException(msg, httpMsg);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
			WebRequest request) {
		
		String msg = "LocalExceptionHandler - HttpMessageNotReadableException - fail";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
	}
}