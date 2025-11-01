package com.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class MessageMetaDTO {

	private String errorType;
	private String messageId;
	private String payload;
	private String oriExchange;
	private String oriRouting;
	private String oriQueue;
	private Integer retryCount;

}