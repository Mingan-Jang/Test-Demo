package com.jjk.upload_test.controller;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UploadController {

	String UPLOAD_DIRECTORY = "./uploads/";

	@GetMapping("/getpath")
	public String getPath() {
		return "imageupload/index";
	}
	
	@GetMapping("/getpath/{id}")
	public String getPathPathVariable(@PathVariable Optional<String> id) {
		 AtomicReference<String> msg = new AtomicReference<>("Id not number");

	        id.ifPresent(idValue -> {
	            System.out.println(idValue);
	            boolean isNumeric = idValue.matches("\\d+");
	            if (isNumeric) {
	                msg.set("Id is number");
	            }
	        });

	        return msg.get();
	}
	
	@GetMapping("/getpathParam")
	public String getPathRequestParam(@RequestParam Optional<String> id) {
		AtomicReference<String> msg = new AtomicReference<>("Id not number"); 
		id.ifPresent(idValue -> {
			System.out.println(idValue);
			boolean isNumeric = idValue.matches("\\d+");
			if (isNumeric) {
				msg.set("Id is number");
			}
		});
		return msg.get();
	}
	
	
	
	
	@PostMapping("/postpath")
	public String postPath(@RequestParam String name) {
		System.out.println(name);
		return "imageupload/index";
	}
	
	@PutMapping("/putpath")
	public String putPath(@RequestParam String name) {
		return "imageupload/index";
	}

	@GetMapping("/uploadimage")
	public String displayUploadForm() {
		return "imageupload/index";
	}


}
