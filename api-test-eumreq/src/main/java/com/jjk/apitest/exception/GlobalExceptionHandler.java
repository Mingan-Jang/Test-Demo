package com.jjk.apitest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
			WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("GlobalExceptionHandler - handleHttpMessageNotReadableException - fail");
	}

	@ExceptionHandler(RestException.class)
	public ResponseEntity<?> handleRestException(RestException ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("GlobalExceptionHandler - handleRestException - fail");
	}
}