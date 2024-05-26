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
import com.jjk.upload_test.dto.ResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/uploads")
public class UploadRestController {

	String UPLOAD_DIRECTORY = "./uploads/";

	@Operation(summary = "Get method, return with HashMap")
	@GetMapping("/getpathReturnMap")
    @ResponseBody
	public  Map<String, String> getpathReturnMap() {
		
		 Map<String, String> response = new HashMap<>();
		    response.put("status", "success");
		    response.put("data", "Some data");
		   return response;
	}

	@Operation(summary = "Get method with @RequestParam, return with HashMap")
	@GetMapping("/getpathRequestParamReturnMap" )
    @ResponseBody
    // 預設 required = true
	public  Map<String, String> getpathRequestParamReturnMap(@RequestParam(name ="name" , required = false) String name ) {
		 Map<String, String> response = new HashMap<>();
		    response.put("status", name);
		    response.put("data", "Some data");
		   return response;
	}
	
	
	@Operation(summary = "Get method with @RequestBody, return with HashMap")
	@GetMapping("/getpathRequestBodyReturnMap" )
    @ResponseBody
    // 預設 required = true
	public  Map<String, String> getpathRequestBodyReturnMap(@RequestBody RequestDTO requestDTO) {

		 Map<String, String> response = new HashMap<>();
		    response.put("status", requestDTO.getName() + " " +requestDTO.getAge());
		    response.put("data", "Some data");
		   return response;
	}
	
	@Operation(summary = "Get method, return with ResponseEntity of an Object")
	@GetMapping("/getpathReturnResponseEntity")
    @ResponseBody
	public  ResponseEntity<?> getpathReturnResponseEntity() {
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setData("Meow");
		responseDTO.setStatus("MeowMeow");
		   return ResponseEntity.ok(responseDTO);
	}
	
	// =========================POST===========================


	@Operation(summary = "Post method @RequestParam, return with HashMap")
	@PostMapping("/postpathRequestParamReturnMap")
    @ResponseBody
	public Map<String, String> postpathRequestParamReturnMap(@RequestParam String name , @RequestParam String data) {
		 Map<String, String> response = new HashMap<>();
		    response.put("status", name);
		    response.put("data", data);
		   return response;
	}
	
	
	@Operation(summary = "Post method @RequestBody, return with HashMap")
	@PostMapping("/postpathRequestBodyReturnMap")
    @ResponseBody
	public Map<String, String> postpathRequestBodyReturnMap(@Valid @RequestBody RequestDTO requestDTO) {
		 Map<String, String> response = new HashMap<>();
		 
		    response.put("status", requestDTO.getName() + " " + requestDTO.getAge());
		    
		    response.put("getDateInDefaultISOFormat", requestDTO.getDateInJSONFormat().toString() );
		    
		    if (requestDTO.getDateInJSONFormat() !=null) {
			    response.put("getDateInCustomFormat", requestDTO.getDateInJSONFormat().toString() );
			    System.out.println(requestDTO.getDateInJSONFormat());
		    }
		    response.put("data", "Testting");
		   return response;
	}

	@Operation(summary = "Post the path @RequestBody , with a RequestDTO object")
	@PostMapping("/postpathRequestBodyReturnResponseEntity")
    @ResponseBody
	public ResponseEntity<?> postpathRequestBodyReturnResponseEntity(@Valid @RequestBody RequestDTO requestDTO) {
		System.out.println(requestDTO.getName());
		System.out.println(requestDTO.getAge());
		
		System.out.println("getDateInJSONFormatGMT08 = " +requestDTO.getDateInJSONFormatGMT08());

		System.out.println("getDateInJSONFormatGMT02 = " +requestDTO.getDateInJSONFormatGMT02());

		System.out.println("getDateInJSONFormatInDEFAULT_TIMEZONE = " + requestDTO.getDateInJSONFormatInDEFAULT_TIMEZONE());


		System.out.println("getDateInJSONFormat = " + requestDTO.getDateInJSONFormat());
		
		System.out.println("getDateInDefaultFormat = " +requestDTO.getDateInDefaultFormat());
		
		System.out.println(requestDTO.getDateSSX());
		System.out.println(requestDTO.getDateSSZ());
		System.out.println(requestDTO.getDateTimeFormatDate());
		System.out.println(requestDTO.getDateTimeFormatDateTime());
		System.out.println(requestDTO.getDateTimeFormatNone());
		System.out.println(requestDTO.getDateTimeFormatTime());

		
		return ResponseEntity.ok(requestDTO);
	}
	
	@Operation(summary = "Post the path with a RequestDTO object")
    @PostMapping(value = "/postpathModelAttribute", consumes = "multipart/form-data")
    public String postPathWithFormData(@ModelAttribute RequestDTO requestDTO) {
        System.out.println(requestDTO.getName());
        System.out.println(requestDTO.getAge());
        System.out.println(requestDTO.getDateInDefaultFormat());
        return "imageupload/index";
    }
	

	@PutMapping("/putpathRequestParam")
	public String putPathRequestParam(@RequestParam String name) {
		System.out.println(name);
		return "imageupload/index";
	}

	@PutMapping("/putpathRequestBody")
	public String postPathWithRuquestBody(@RequestBody RequestDTO requestDTO) {
		System.out.println(requestDTO.getName());
		System.out.println(requestDTO.getAge());
		System.out.println(requestDTO.getDateInDefaultFormat());
		return "imageupload/index";
	}

	@GetMapping("/uploadimage")
	public String displayUploadForm() {
		return "imageupload/index";
	}

	
	
	
	@PostMapping("/upload")
	public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
		StringBuilder fileNames = new StringBuilder();

		// 定義文件的存儲路徑和文件名
		Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());

		// 將文件名添加到StringBuilder中
		fileNames.append(file.getOriginalFilename());

		// 將文件的字節數據寫入指定路徑
		Files.write(fileNameAndPath, file.getBytes());

		// 將上傳成功的消息添加到模型中，以便在視圖中顯示
		model.addAttribute("msg", "Uploaded images: " + fileNames.toString());

		// 返回視圖"imageupload/index"
		return "imageupload/index";
	}

}
