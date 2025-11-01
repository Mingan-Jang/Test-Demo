package com.event.dto;

import com.event.enumerations.DlxStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class DeadLetterMessageDTO extends MessageMetaDTO {
	private String dlxId;
	private String dlxExchange;
	private String dlxRouting;
	private String dlxQueue;
	private DlxStatusEnum status;
	private String errorDesc;
	private String dlxType;
}