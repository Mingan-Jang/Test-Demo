package com.jjk.upload_test.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;

@Controller
@RequestMapping("/uploads")
public class UploadController {

	String UPLOAD_DIRECTORY = "uploads/";

	@Operation(summary = "Get the HTML template")
	@GetMapping("/getpath")
	public String getPath() {
		return "imageupload/index";
	}

	@Operation(summary = "Upload the file")
	@PostMapping("/upload")
	public String uploadImage(Model model, @RequestParam("image") MultipartFile[] files) throws IOException {
		StringBuilder fileNames = new StringBuilder();

		try {
			for (MultipartFile file : files) {
				// 定義文件的存儲路徑和文件名
				Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());

				// 將文件名添加到StringBuilder中
				fileNames.append(file.getOriginalFilename());

				// 將文件的字節數據寫入指定路徑
				Files.write(fileNameAndPath, file.getBytes());

				System.out.println(fileNameAndPath.toAbsolutePath());
				// 將上傳成功的消息添加到模型中，以便在視圖中顯示
				model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
			}
		} catch (MaxUploadSizeExceededException e) {
			System.out.println("SSSSS");
			model.addAttribute("msg", "Upload fail");
		}

		System.out.println("SSSSS");
		return "redirect:/uploads/getpath";
	}

}
