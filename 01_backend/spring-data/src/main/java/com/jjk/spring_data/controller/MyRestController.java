package com.jjk.spring_data.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jjk.spring_data.po.ImportPO;
import com.jjk.spring_data.service.ImportService;

@RestController
public class MyRestController {

	private final ImportService importService;

	@Autowired
	public MyRestController(ImportService importService) {
		this.importService = importService;
	}

	@GetMapping("/findAll/jpql/complete")
	public ResponseEntity<List<ImportPO>> getAllByJPQL() {
		List<ImportPO> importPOs = importService.findAllWithAgeGreaterThanZeroJPQL();
		return ResponseEntity.ok(importPOs);
	}

	@GetMapping("/findAll/sql/complete")
	public ResponseEntity<List<ImportPO>> getAllBySQL() {
		List<ImportPO> importPOs = importService.findAllWithAgeGreaterThanZeroSQL();
		return ResponseEntity.ok(importPOs);
	}

	@GetMapping("/findAll/jpql/part")
	public ResponseEntity<List<ImportPO>> getAllByJPQLPart() {
		List<ImportPO> importPOs = importService.findAllWithAgeGreaterThanZeroJPQLPart();
		return ResponseEntity.ok(importPOs);
	}

	@GetMapping("/findAll/sql/part")
	public ResponseEntity<?> getAllBySQLPart() {
		return ResponseEntity.ok(importService.findAllWithAgeGreaterThanZeroSQLPart());
	}

}
