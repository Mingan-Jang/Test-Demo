package com.example.demoweb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demoweb.jxls.AutoRowHeightCommand;
import com.example.demoweb.po.Employee;
import com.example.demoweb.po.People;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	@Operation(summary = "下載報表 (sort by row)")
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
	@Operation(summary = "下載報表 (sort by column)")
	public void get2(HttpServletResponse response) {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee("史丹利", new Date(), new BigDecimal("5000"), new BigDecimal("1000")));
		employees.add(new Employee("瑪莉", new Date(), new BigDecimal("6000"), new BigDecimal("1200")));
		employees.add(new Employee("布萊恩", new Date(), new BigDecimal("5500"), new BigDecimal("1100")));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		System.out.println("Classpath: " + resource.getPath());

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/test2.xlsx")) {
			// 設置檔頭資訊 編碼
			String fileName = URLEncoder.encode("測試表", "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf8");

			OutputStream os = response.getOutputStream();
			Context context = new Context();
			context.putVar("employees", employees); // 名稱對應excel的items
			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/download3")
	public void get3(HttpServletResponse response) {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee("史丹利", new Date(), new BigDecimal("5000"), new BigDecimal("1000")));
		employees.add(new Employee("瑪莉", new Date(), new BigDecimal("6000"), new BigDecimal("1200")));
		employees.add(new Employee("布萊恩", new Date(), new BigDecimal("5500"), new BigDecimal("1100")));

		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		System.out.println("Classpath: " + resource.getPath());

		try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/test3.xlsx")) {
			// 設置檔頭資訊 編碼
			String fileName = URLEncoder.encode("測試表", "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf8");

			OutputStream os = response.getOutputStream();
			Context context = new Context();
			context.putVar("employees", employees); // 名稱對應excel的items
			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@GetMapping("/download4")
	@Operation(summary = "下載報表 (Adjust Row Height")
	public void adjustRowHeight(HttpServletResponse response) {
		List<People> peoples = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("史丹利").append(" ").append(33).append(" ").append("男").append("\n").append("難");

		peoples.add(new People("史丹利", 33, sb.toString()));
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
			
//	        XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);

			JxlsHelper.getInstance().setEvaluateFormulas(true).processTemplate(is, os, context);
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
