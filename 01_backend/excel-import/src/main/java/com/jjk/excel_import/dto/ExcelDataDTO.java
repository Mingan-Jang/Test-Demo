package com.jjk.excel_import.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDataDTO {
	// EventDTO
	private String occurTime;
	// EventDTO
	private String operator;
	// EventDTO
	private String categoryName;
	// EventDTO
	private String categoryType;
	// EventDTO
	private String occurDesc;
	
	// ExtendDTO
	private String occurDescThird;
	
	// EventLocationDTO
	private String originStation;
	private String destinationStation;
	
	// ExtendDTO
	private String locationCity;
	
	// 
	private Integer inEmployeeDeaths;
	private Integer inPassengerDeaths;
	private Integer inCivilianDeaths;
	private Integer inEmployeeInjuries;
	private Integer inPassengerInjuries;
	private Integer inCivilianInjuries;
	
	// EventLocationDTO
	private String trainNo;
	
	// ExtendDTO
	private String trainType;
	private String trainModel;
	private String vehNo;
	
	// ExtendDTO
	private String improveTime;
	private String impactTime;
	
	// EventDTO
	private String authority;
	private String locationNativeLang;
	private String locationRemark;
	private String cityRemark;
	
	
	private Integer totalDeaths; // 11+12+13
	private Integer totalInjuries; // 14+15+16
	
	
	// EventDTO
	private String equipStatusDesc;
	private String siteStatusDesc;
	private String improvementSuggestion;
	private String casualtyEquipStatus;
	
	
	// EventDTO.operatingStatusInterruptionDesc
	private String operationDesc;
	
	// ExtendDTO
	private Integer outEmployeeDeaths;
	private Integer outPassengerDeaths;
	private Integer outCivilianDeaths;
	private Integer outEmployeeInjuries;
	private Integer outPassengerInjuries;
	private Integer outCivilianInjuries;
	
	// EventDTO
	private String comment;
	
	// ExtendDTO
	private String dailyReport;
}
