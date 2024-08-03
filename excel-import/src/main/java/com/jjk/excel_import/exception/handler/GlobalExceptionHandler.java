package com.jjk.excel_import.exception.handler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jjk.excel_import.exception.RestException;
import com.jjk.excel_import.exception.RestOutException;
import com.jjk.excel_import.exception.model.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(RestException.class)
	protected ResponseEntity<Object> handleRestException(RestException ex, WebRequest request) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(ex.getErrorCode());
		response.setMessage(ex.getMessage());
		response.setTimestamp(ZonedDateTime.now());

		
		Map<String, Object> map = new HashMap<>();
		map.put("error", response);

		return new ResponseEntity<Object>(map, ex.getHttpStatus());
	}
	
	
	@ExceptionHandler(RestOutException.class)
	protected ResponseEntity<Object> handleRestOutException(RestOutException ex, WebRequest request) {
	    log.error("Exception Occured: ", ex);

		ErrorResponse response = new ErrorResponse();
		response.setCode(ex.getErrorCode());
		response.setMessage(ex.getMessage());
		response.setTimestamp(ZonedDateTime.now());

		
		Map<String, Object> map = new HashMap<>();
		map.put("error", response);

		return new ResponseEntity<Object>(map, ex.getHttpStatus());
	}
}
