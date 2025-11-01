package com.event.service;

import java.net.SocketTimeoutException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;

import com.event.config.RabbitMqConfig;
import com.event.dto.DeadLetterMessageDTO;
import com.event.dto.EventDTO;
import com.event.dto.MessageMetaDTO;
import com.event.enumerations.ActionEnum;
import com.event.enumerations.DlxErrorTypeEnum;
import com.event.enumerations.DlxStatusEnum;
import com.event.enumerations.ErrorMockEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventMqService extends PreProcessService {

	private final ObjectMapper objectMapper;
	private final RabbitTemplate rabbitTemplate;
	private final ProcessService processService;
	private final EventService eventService;

	private static final int MAX_RETRIES = 3;
	private static final int PROCESSING_DELAY_MS = 3000;
	private static final ErrorMockEnum ERROR_MODE = ErrorMockEnum.SOCKET_TIMEOUT;
	private static final int[] RETRY_DELAYS_MS = { 1000, 3000, 4000, 4000 };

	public void handleEvent(EventDTO event, Message amqpMessage, Channel channel, long tag) throws Exception {

		String eventJson = objectMapper.writeValueAsString(event);


		MessageMetaDTO baseMeta = MessageMetaDTO.builder().messageId(amqpMessage.getMessageProperties().getMessageId())
				.payload(eventJson).oriExchange(amqpMessage.getMessageProperties().getReceivedExchange())
				.oriRouting(amqpMessage.getMessageProperties().getReceivedRoutingKey())
				.oriQueue(amqpMessage.getMessageProperties().getConsumerQueue()).retryCount(getRetryCount(amqpMessage))
				.build();
		String messageId = baseMeta.getMessageId();
		event.setMessageId(messageId);
		
		log.info("開始處理消息 [messageId={}, retry={}, routing={}]", messageId, baseMeta.getRetryCount(),
				baseMeta.getOriRouting());

		try {
			// 模擬不穩定處理
			// simulateUnstableProcessing(messageId);
			validateEvent(event);

			if (StringUtils.containsIgnoreCase(baseMeta.getOriRouting(), ActionEnum.CREATE.name())) {
				if (eventService.findByMessageId(messageId) != null) {
					sendToDLX(baseMeta, DlxErrorTypeEnum.DUP.name(), "資料重複");
					log.warn("業務錯誤資料重複，手動 DLX [messageId={}]", messageId);
					channel.basicAck(tag, false);
					return;
				}
				processService.processCreate(event);

			} else {

				EventDTO oldEvent = eventService.findByMessageId(messageId);
				if (eventService.findByMessageId(messageId) == null) {
					sendToDLX(baseMeta, DlxErrorTypeEnum.NF.name(), "原始事件不存在");
					log.warn("業務錯誤原始事件不存在，手動 DLX [messageId={}]", messageId);
					channel.basicAck(tag, false);
					return;
				}
				processService.processUpdate(event, oldEvent);
			}

			channel.basicAck(tag, false);
			log.info(" 消息處理成功 [messageId={}]", messageId);

		} catch (ValidationException e) {
			sendToDLX(baseMeta, DlxErrorTypeEnum.VAL.name(), e.getMessage());
			log.warn("驗證錯誤，手動 DLX [messageId={}]", messageId);
			channel.basicAck(tag, false);

		} catch (Exception e) {
			int nextRetry = baseMeta.getRetryCount() + 1;
			if (nextRetry > MAX_RETRIES) {
				sendToDLX(baseMeta, DlxErrorTypeEnum.RETRY.name(), "超過最大重試次數");
				log.error("超過最大重試次數，送入手動死信 [messageId={}]", messageId);
				channel.basicAck(tag, false);
				return;
			}

			int delay = (baseMeta.getRetryCount() < RETRY_DELAYS_MS.length) ? RETRY_DELAYS_MS[baseMeta.getRetryCount()]
					: RETRY_DELAYS_MS[RETRY_DELAYS_MS.length - 1];

			// 手動送到延遲交換器
			rabbitTemplate.convertAndSend(RabbitMqConfig.PROCESS_DELAY_EXCHANGE, baseMeta.getOriRouting(), event,
					msg -> {
						msg.getMessageProperties().setMessageId(messageId);
						msg.getMessageProperties().setHeader("x-delay", delay);
						msg.getMessageProperties().setHeader("x-retry-count", nextRetry);
						return msg;
					});

			log.warn("系統錯誤，消息將在 {}ms 後重試 [messageId={}, retry={}]", delay, messageId, nextRetry, e);
			channel.basicAck(tag, false); // ✅ 已重派到延遲交換器 → ACK

		}
	}

	private void sendToDLX(MessageMetaDTO baseMeta, String errorType, String errorDesc) {
		DeadLetterMessageDTO dlx = DeadLetterMessageDTO.builder().dlxId(UUID.randomUUID().toString())
				.status(DlxStatusEnum.N).errorType(errorType).errorDesc(errorDesc)
				.dlxExchange(RabbitMqConfig.DLX_EXCHANGE).dlxRouting(RabbitMqConfig.DLX_ROUTING_KEY_MANUAL)
				.dlxQueue(RabbitMqConfig.DLX_QUEUE).messageId(baseMeta.getMessageId()).payload(baseMeta.getPayload())
				.oriExchange(baseMeta.getOriExchange()).oriRouting(baseMeta.getOriRouting())
				.oriQueue(baseMeta.getOriQueue()).retryCount(baseMeta.getRetryCount()).build();

		rabbitTemplate.convertAndSend(RabbitMqConfig.DLX_EXCHANGE, RabbitMqConfig.DLX_ROUTING_KEY_MANUAL, dlx);

	}

	private int getRetryCount(Message message) {
		Object retryHeader = message.getMessageProperties().getHeaders().get("x-retry-count");
		if (retryHeader instanceof Number)
			return ((Number) retryHeader).intValue();
		return 0;
	}

	private void simulateUnstableProcessing(String messageId) throws Exception {
		switch (ERROR_MODE) {
		case SOCKET_TIMEOUT -> throw new SocketTimeoutException("模擬網路超時 [messageId=" + messageId + "]");
		case QUERY_TIMEOUT -> {
			Thread.sleep(PROCESSING_DELAY_MS);
			throw new QueryTimeoutException("模擬查詢超時 [messageId=" + messageId + "]");
		}
		case RUNTIME -> throw new RuntimeException("模擬系統異常 [messageId=" + messageId + "]");
		}
	}

}