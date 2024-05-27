package com.example.demoweb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demoweb.jxls.AutoRowHeightCommand;
import com.example.demoweb.jxls.HideColumnCommand;
import com.example.demoweb.po.People;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Tag(name = "Jxls")
public class JxlsController {


	@GetMapping("/test")
	@ResponseBody
	public List<People> test(HttpServletResponse response) {
		List<People> peoples = new ArrayList<>();
		peoples.add(new People("史丹利", 33, "男"));
		peoples.add(new People("瑪莉", 20, "女"));
		peoples.add(new People("布萊恩", 25, "男"));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("aaa.txt");
		System.out.println("Classpath: " + resource.getPath());

		return peoples;
	}

	@GetMapping("/download")
	@Operation(summary = "Sort by row")
	public void sortByRow(HttpServletResponse response) {
		List<People> peoples = new ArrayList<>();
		peoples.add(new People("史丹利", 33, "男"));
		peoples.add(new People("瑪莉", 20, "女"));
		peoples.add(new People("布萊恩", 25, "男"));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		System.out.println("Classpath: " + resource.getPath());

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/REPORT_ROW.xlsx")) {
			// 設置檔頭資訊 編碼
			String fileName = URLEncoder.encode("測試表", "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf8");

			OutputStream os = response.getOutputStream();
			Context context = new Context();
			context.putVar("peoples", peoples); // 名稱對應excel的items
			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/download2")
	@Operation(summary = "Sort by column")
	public void get2(HttpServletResponse response) {
		List<People> peoples = new ArrayList<>();
		peoples.add(new People("史丹利", 33, "男"));
		peoples.add(new People("瑪莉", 20, "女"));
		peoples.add(new People("布萊恩", 25, "男"));
		peoples.add(new People("Meow", 999, "男"));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		System.out.println("Classpath: " + resource.getPath());

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/REPORT_COL.xlsx")) {
			// 設置檔頭資訊 編碼
			String fileName = URLEncoder.encode("測試表", "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf8");

			OutputStream os = response.getOutputStream();
			Context context = new Context();
			context.putVar("peoples", peoples); // 名稱對應excel的items
			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/download3")
	@Operation(summary = "Adjust Row Height")
	public void get3(HttpServletResponse response) {
		List<People> peoples = new ArrayList<>();
		peoples.add(new People("史丹利", 33, "男"));
		peoples.add(new People("瑪莉", 20, "女"));
		peoples.add(new People("布萊恩", 25, "男"));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		System.out.println("Classpath: " + resource.getPath());

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/REPORT_ROW_HEIGHT.xlsx")) {
			// 設置檔頭資訊 編碼
			String fileName = URLEncoder.encode("測試表", "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf8");

			OutputStream os = response.getOutputStream();
			Context context = new Context();
			context.putVar("peoples", peoples); // 名稱對應excel的items
			
	        XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);

			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/download4")
	@Operation(summary = "Hide the column using EXCEL POI")
	public void hideColumn1(HttpServletResponse response) {
		 List<People> peoples = new ArrayList<>();
	        StringBuilder sb = new StringBuilder();
	        sb.append("史丹利").append(" ").append(33).append(" ").append("男").append("\n").append("難");

	        peoples.add(new People("史丹利", 33, sb.toString()));
	        peoples.add(new People("瑪莉", 20, "女"));
	        peoples.add(new People("布萊恩", 25, "男"));

	        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/REPORT_ROW_HIDE.xlsx");
	             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

	            // 設置檔頭資訊 編碼
	            String fileName = URLEncoder.encode("測試表", "UTF-8");
	            response.setHeader("Content-Disposition",
	                    "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
	            response.setContentType("application/vnd.ms-excel;charset=utf8");

	            // 创建 Jxls context
	            Context context = new Context();
	            context.putVar("peoples", peoples); // 名稱對應excel的items

	            // 使用 Jxls 处理模板
	            JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, bos, context);

	            // 使用 POI 隐藏特定列
	            try (InputStream bis = new ByteArrayInputStream(bos.toByteArray());
	                 Workbook workbook = WorkbookFactory.create(bis);
	                 ServletOutputStream sos = response.getOutputStream()) {

	                Sheet sheet = workbook.getSheetAt(0);
	                sheet.setColumnHidden(1, true); // 隐藏第2列

	                workbook.write(sos);
	                sos.flush();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	
	@GetMapping("/download5")
	@Operation(summary = "Hide the column using Command")
	public void hideColumn2(HttpServletResponse response) {
		List<People> peoples = new ArrayList<>();
		peoples.add(new People("史丹利", 12, "男"));
		peoples.add(new People("瑪莉", 23, "女"));
		peoples.add(new People("布萊恩", 25, "男"));
		peoples.add(new People("布萊恩2", 33, "男"));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		System.out.println("Classpath: " + resource.getPath());

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/REPORT_ROW_HIDE.xlsx")) {
			// 設置檔頭資訊 編碼
			String fileName = URLEncoder.encode("測試表", "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf8");

			OutputStream os = response.getOutputStream();
			Context context = new Context();
			context.putVar("peoples", peoples); // 名稱對應excel的items
			
	        XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);
	        XlsCommentAreaBuilder.addCommandMapping("hiddenDef", HideColumnCommand.class);

			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
