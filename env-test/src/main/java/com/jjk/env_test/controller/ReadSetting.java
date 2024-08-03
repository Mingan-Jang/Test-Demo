package com.jjk.env_test.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "ReadSetting", description = "Read setting management APIs with https://hackmd.io/@5_PlEE5DTQ-da42CnSOhOA/r1qHCNSHA")
public class ReadSetting {

	@Value("${myspringvar.var1}")
	private String var1;

	@Value("${sp.defult_var}")
	private String mvnVar;

	@Value("${sp.dev_var}")
	private String mvnVarFromProfile;

	@Operation(summary = "從 System.getProperty 獲取變量")
	@GetMapping("/getSystemProperty1")
	public String getSystemProperty() {

		return System.getProperty("MyVar.TestEnvVar1");
	}

	@Operation(summary = "從 spring property 獲取變量")
	@GetMapping("/getSpringProperty1")
	public String getSpringProperty() {
		return var1;
	}

	@Operation(summary = "從 maven property 獲取變量")
	@GetMapping("/getMavenProperty")
	public String getMavenProperty() {
		return mvnVar;
	}

	@Operation(summary = "從配置文件中獲取 maven property 變量")
	@GetMapping("/getMavenPropertyFromProfile")
	public String getMavenPropertyFromProfile() {
		return mvnVarFromProfile;
	}

	@Operation(summary = "獲取 Java 版本")
	@GetMapping("/getJavaVersion")
	public String getJavaVersion() {
		return System.getProperty("java.version");
	}

}
