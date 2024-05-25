package com.jjk.upload_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/uploads")
public class UploadRestController {

	String UPLOAD_DIRECTORY = "./uploads/";

	@GetMapping("/getpath")
	public String getPath() {
		return "imageupload/index";
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
