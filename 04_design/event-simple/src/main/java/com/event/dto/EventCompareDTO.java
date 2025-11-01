package com.event.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "事件資料變更比較 DTO")
public class EventCompareDTO {

	@Schema(description = "舊的事件資料")
	private EventDTO oldEvent;

	@Schema(description = "新的事件資料")
	private EventDTO newEvent;

	@Schema(description = "變更欄位詳細資訊")
	private List<FieldChangeDTO> fieldChanges;

}
