package com.jjk.excel_import.po;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "rb_event_extended_info")
public class ExtendPO {

	@Column(name = "extended_info_id")
	private String extendedInfoId;

	@Column(name = "occur_desc_third")
	private String occurDescThird;

	@Column(name = "location_city")
	private String locationCity;

	@Column(name = "train_type")
	private String trainType;

	@Column(name = "train_model")
	private String trainModel;

	@Column(name = "veh_no")
	private String vehNo;

	@Column(name = "improve_time")
	private Instant improveTime;

	@Column(name = "impact_time")
	private Instant impactTime;

	private String authority;

	@Column(name = "location_native_lang")
	private String locationNativeLang;

	@Column(name = "location_remark")
	private String locationRemark;

	@Column(name = "city_remark")
	private String cityRemark;

	@Column(name = "casualty_equip_status")
	private String casualtyEquipStatus;

	@Column(name = "out_employee_deaths")
	private Integer outEmployeeDeaths;

	@Column(name = "out_passenger_deaths")
	private Integer outPassengerDeaths;

	@Column(name = "out_civilian_deaths")
	private Integer outCivilianDeaths;

	@Column(name = "out_employee_injuries")
	private Integer outEmployeeInjuries;

	@Column(name = "out_passenger_injuries")
	private Integer outPassengerInjuries;

	@Column(name = "out_civilian_injuries")
	private Integer outCivilianInjuries;

	@Column(name = "daily_report")
	private String dailyReport;

}