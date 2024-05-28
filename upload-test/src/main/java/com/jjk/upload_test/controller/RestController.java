package com.jjk.upload_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api")
public class RestController {

	String UPLOAD_DIRECTORY = "./uploads/";

	@GetMapping("/get/{id}")
	@Operation(summary = "Get path variable", description = "Check if the path variable is numeric")
	public String getPathPathVariable(@PathVariable Optional<String> id) {
	    AtomicReference<String> msg = new AtomicReference<>("Id is not a number");

	    id.ifPresent(idValue -> {
	        System.out.println(idValue);
	        boolean isNumeric = idValue.matches("\\d+");
	        if (isNumeric) {
	            msg.set("Id is a number");
	        }
	    });

	    return msg.get();
	}

	@GetMapping("/getparam")
	@Operation(summary = "Get request parameter", description = "Check if the request parameter is numeric")
	public String getPathRequestParam(@RequestParam Optional<String> id) {
	    AtomicReference<String> msg = new AtomicReference<>("Id is not a number");
	    id.ifPresent(idValue -> {
	        System.out.println(idValue);
	        boolean isNumeric = idValue.matches("\\d+");
	        if (isNumeric) {
	            msg.set("Id is a number");
	        }
	    });
	    return msg.get();
	}

	@GetMapping("/getreturnmap")
	@Operation(summary = "Get method return map", description = "Get method that returns a HashMap")
	@ResponseBody
	public Map<String, String> getpathReturnMap() {
	    Map<String, String> response = new HashMap<>();
	    response.put("status", "success");
	    response.put("data", "Some data");
	    return response;
	}

	@GetMapping("/getrequestparamreturnmap" )
	@Operation(summary = "Get method with request parameter return map", description = "Get method that returns a HashMap with request parameter")
	@ResponseBody
	public Map<String, String> getpathRequestParamReturnMap(@RequestParam(name ="name" , required = false) String name ) {
	    Map<String, String> response = new HashMap<>();
	    response.put("status", name);
	    response.put("data", "Some data");
	    return response;
	}

	@GetMapping("/getrequestbodyreturnmap" )
	@Operation(summary = "Get method with request body return map", description = "Get method that returns a HashMap with request body")
	@ResponseBody
	public Map<String, String> getpathRequestBodyReturnMap(@RequestBody RequestDTO requestDTO) {
	    Map<String, String> response = new HashMap<>();
	    response.put("status", requestDTO.getName() + " " +requestDTO.getAge());
	    response.put("data", "Some data");
	    return response;
	}

	@GetMapping("/getreturnresponseentity")
	@Operation(summary = "Get method return response entity", description = "Get method that returns ResponseEntity of an Object")
	@ResponseBody
	public ResponseEntity<?> getpathReturnResponseEntity() {
	    ResponseDTO responseDTO = new ResponseDTO();
	    responseDTO.setData("Meow");
	    responseDTO.setStatus("MeowMeow");
	    return ResponseEntity.ok(responseDTO);
	}

	
	// =========================POST===========================

	@Operation(summary = "Post method with @RequestParam, return with HashMap")
	@PostMapping("/postRequestParamReturnMap")
	@ResponseBody
	public Map<String, String> postRequestParamReturnMap(@RequestParam String name, @RequestParam String data) {
	    Map<String, String> response = new HashMap<>();
	    response.put("status", name);
	    response.put("data", data);
	    return response;
	}

	@Operation(summary = "Post method with @RequestBody, return with HashMap")
	@PostMapping("/postRequestBodyReturnMap")
	@ResponseBody
	public Map<String, String> postRequestBodyReturnMap(@Valid @RequestBody RequestDTO requestDTO) {
	    Map<String, String> response = new HashMap<>();
	    response.put("status", requestDTO.getName() + " " + requestDTO.getAge());
	    response.put("name", requestDTO.getName());
	    response.put("data", "Testing");
	    return response;
	}

	@Operation(summary = "Post method with @RequestBody, return with ResponseEntity of a RequestDTO object")
	@PostMapping("/postRequestBodyReturnResponseEntity")
	@ResponseBody
	public ResponseEntity<?> postRequestBodyReturnResponseEntity(@Valid @RequestBody RequestDateDTO requestDateDTO) {
	    System.out.println(requestDateDTO.getName());
	    System.out.println(requestDateDTO.getAge());
	    System.out.println("getDateInJSONFormatGMT08 = " + requestDateDTO.getDateInJSONFormatGMT08());
	    System.out.println("getDateInJSONFormatGMT02 = " + requestDateDTO.getDateInJSONFormatGMT02());
	    System.out.println("getDateInJSONFormatInDEFAULT_TIMEZONE = " + requestDateDTO.getDateInJSONFormatInDEFAULT_TIMEZONE());
	    System.out.println("getDateInJSONFormat = " + requestDateDTO.getDateInJSONFormat());
	    System.out.println("getDateInDefaultFormat = " + requestDateDTO.getDateInDefaultFormat());
	    System.out.println(requestDateDTO.getDateSSX());
	    System.out.println(requestDateDTO.getDateSSZ());
	    System.out.println(requestDateDTO.getDateTimeFormatDate());
	    System.out.println(requestDateDTO.getDateTimeFormatDateTime());
	    System.out.println(requestDateDTO.getDateTimeFormatNone());
	    System.out.println(requestDateDTO.getDateTimeFormatTime());
	    return ResponseEntity.ok(requestDateDTO);
	}

	@Operation(summary = "Post method with multipart/form-data, return with a RequestDTO object")
	@PostMapping(value = "/postFormData", consumes = "multipart/form-data")
	public ResponseEntity<?> postFormData(@ModelAttribute RequestDTO requestDTO) {
	    System.out.println(requestDTO.getName());
	    System.out.println(requestDTO.getAge());
	    return ResponseEntity.ok(requestDTO);
	}

	@Operation(summary = "Post method with application/x-www-form-urlencoded, return with a RequestDTO object")
	@PostMapping(value = "/postUrlencoded", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<?> postUrlencoded(@ModelAttribute RequestDTO requestDTO) {
	    System.out.println(requestDTO.getName());
	    System.out.println(requestDTO.getAge());
	    return ResponseEntity.ok(requestDTO);
	}
	
	// ======================== PUT =========================
	// 跟 put 完全相同

	
	
	// ========================== DELETE ========================

	
	
	
	

}
