package com.jjk.apitest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApiTestRequestDTO {
	@Schema(defaultValue = "25.0330")
	private double lat;
	@Schema(defaultValue = "121.5654")
	private double lon;
	@Schema(defaultValue = "ROOFTOP")
	private String locationType = "ROOFTOP";
	@Schema(defaultValue = "zh-TW")
	private String language = "zh-TW";
	@Schema(defaultValue = "street_address")
	private String resultType = "street_address";

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	@Override
	public String toString() {
		return "ReverseGeoDTO{" + "lat=" + lat + ", lon=" + lon + ", locationType='" + locationType + '\''
				+ ", language='" + language + '\'' + ", resultType='" + resultType + '\'' + '}';
	}
}
