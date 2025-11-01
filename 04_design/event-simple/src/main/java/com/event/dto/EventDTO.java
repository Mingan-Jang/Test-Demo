package com.event.dto;

import java.time.LocalDateTime;

import com.event.enumerations.ActionEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "事件資料傳輸物件")
public class EventDTO {
	@Schema(description = "事件唯一識別 ID", example = "123e4567-e89b-12d3-a456-426614174000")
	private String eventId;

	@Schema(description = "發生時間", example = "2025-09-14T14:30:00")
	private LocalDateTime occurTime;

	private String messageId;

	@Schema(description = "事件類型", example = "INCIDENT")
	private String eventType;

	@Schema(description = "優先等級", example = "HIGH")
	private String priority;

	@Schema(description = "事件描述", example = "月台異常警示")
	private String description;

	@Schema(description = "來源", example = "mq")
	private String soce;

	@JsonIgnore
	private ActionEnum action;

}