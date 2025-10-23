package com.jjk.excel_import.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLocationDTO {
	@Id
	private String eventOid;

	private double lat;

	private double lng;

	private String lineId;

	private String originStation;

	private String destinationStation;

	private String trainNo;
}
