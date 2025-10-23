package com.jjk.excel_import.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jjk.excel_import.enumer.Err;
import com.jjk.excel_import.exception.RestException;
import com.jjk.excel_import.exception.RestOutException;
import com.jjk.excel_import.req.ImportExcelReqDTO;
import com.jjk.excel_import.service.ExcelDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "excel API")
public class ImporExcelController {

	@Autowired
	ExcelDataService excelDataService;
	
	@PostMapping("/excel/insert")
	@Operation(summary = "把月統計報表寫入DB中")
	public ResponseEntity<?> insert(@RequestBody ImportExcelReqDTO importExcelReqDTO) {
		if (importExcelReqDTO == null || importExcelReqDTO.getPath() == null) {
			throw new RestOutException(Err.BAD_REQUEST.getCode(), "path 不得為空");
//			ResGeneralDTO res = ResGeneralDTO.builder().errcode(HttpStatus.BAD_REQUEST.value())
//					.errorMessage("path 不得為空").build();
//			return ResponseEntity.badRequest().body(res);
		}
		
		List<Object[]> objList = excelDataService.readExcelFile(importExcelReqDTO.getPath());
		excelDataService.insertIntoDatabase(objList);

		
		return ResponseEntity.ok().body("success");
	}
	
	
	
	
	// Exception

	@PostMapping("/excel/test-exception-with-catch")
	@Operation(summary = "Test RestException with catch, ExceptionHandler inside the controller")
	public ResponseEntity<?> testExceptionWithCatch() {
		try {
			callRestException();
		} catch (RestException e) {
			throw e;
		}

		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/excel/test-exception-without-catch")
	@Operation(summary = "Test RestException without catch, ExceptionHandler inside the controller", description = "回傳500")
	public ResponseEntity<?> testExceptionWithoutCatch() {

		try {
			callRestException();
		} catch (RestException e) {
			throw e;
		}

		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/excel/test-out-exception-without-catch")
	@Operation(summary = "Test RestOutException without catch, GlobalExceptionHandler outside the controller")
	public ResponseEntity<?> testOutExceptionWithoutCatch2() {
		callRestOutException();

		return ResponseEntity.ok().body(null);
	}

	@PostMapping("/excel/test-out-exception-with-catch")
	@Operation(summary = "Test RestOutException with catch, GlobalExceptionHandler outside the controller")
	public ResponseEntity<?> testOutExceptionWithCatch() {

		try {
			callRestOutException();
		} catch (RestException e) {
			throw e;
		}
		return ResponseEntity.ok().body(null);
	}

	public void callRestException() {

		throw new RestException(HttpStatus.BAD_REQUEST.toString(), "My Error Msg");
	}

	public void callRestOutException() {

		throw new RestOutException(HttpStatus.BAD_REQUEST.toString(), "My Outer Error Msg");
	}

	@ExceptionHandler(RestException.class)
	public ResponseEntity<String> handleRestException(RestException ex) {
		HttpStatus status = ex.getHttpStatus();
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR; // Default status if parsing fails
		}
		return new ResponseEntity<>(ex.getMessage(), status);
	}
}
