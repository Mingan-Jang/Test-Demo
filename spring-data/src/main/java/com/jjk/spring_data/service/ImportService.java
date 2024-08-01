package com.jjk.spring_data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jjk.spring_data.dao.ImportDAO;
import com.jjk.spring_data.po.ImportPO;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ImportService {

	private final ImportDAO importDAO;

	@Autowired
	public ImportService(ImportDAO importDAO) {
		this.importDAO = importDAO;
	}

	public List<ImportPO> findAllWithAgeGreaterThanZeroJPQL() {
		return importDAO.findImportPOsWithAgeGreaterThanZero();
	}

	public List<ImportPO> findAllWithAgeGreaterThanZeroSQL() {
		return importDAO.findImportPOsWithAgeGreaterThanZeroSQL();
	}

	public List<ImportPO> findAllWithAgeGreaterThanZeroJPQLPart() {
		return importDAO.findImportPOsWithAgeGreaterThanZeroPart();
	}

	public List<?> findAllWithAgeGreaterThanZeroSQLPart() {
		return importDAO.findImportPOsWithAgeGreaterThanZeroSQLPart();
	}
}