package com.event.listener;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.event.config.RabbitMqConfig;
import com.event.dao.postgres.DeadLetterMessageDAO;
import com.event.dto.DeadLetterMessageDTO;
import com.event.enumerations.DlxStatusEnum;
import com.event.mapper.DeadLetterMapper;
import com.event.po.postgres.DeadLetterMessagePO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeadLetterListener {

	private final DeadLetterMessageDAO deadLetterMessageDAO;
	private final ObjectMapper objectMapper;

	@RabbitListener(queues = RabbitMqConfig.DLX_QUEUE)
	public void handleDeadLetter(Message message) {

		String routingKey = message.getMessageProperties().getReceivedRoutingKey();
		String messageId = message.getMessageProperties().getMessageId();
		log.info("接收到死信消息 [messageId={}, routingKey={}]", messageId, routingKey);

		try {
			DeadLetterMessagePO po;

			if (RabbitMqConfig.DLX_ROUTING_KEY_MANUAL.equals(routingKey)) {
				String body = new String(message.getBody(), StandardCharsets.UTF_8);

				log.info("the body:{}", body);

				DeadLetterMessageDTO dto = objectMapper.readValue(body, DeadLetterMessageDTO.class);
				po = DeadLetterMapper.INSTANCE.toPO(dto);
				po.setDlxType("MANUAL");
			} else {
				po = new DeadLetterMessagePO();
				po.setMessageId(messageId);
				po.setDlxId(UUID.randomUUID().toString());
				po.setDlxExchange(message.getMessageProperties().getReceivedExchange());
				po.setDlxRouting(routingKey);
				po.setDlxQueue(message.getMessageProperties().getConsumerQueue());
				parseXDeathHeader(message, po);
			}

			// 直接存，不管是否重複
			deadLetterMessageDAO.save(po);
			log.info("死信消息已保存到資料庫 [messageId={}, status={}]", po.getMessageId(), po.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
			log.error("處理死信消息時發生錯誤 [messageId={}]", messageId, e);
		}

	}

	@SuppressWarnings("unchecked")
	private void parseXDeathHeader(Message message, DeadLetterMessagePO po) {
		try {
			Map<String, Object> headers = message.getMessageProperties().getHeaders();
			Object xDeathObj = headers.get("x-death");

			po.setDlxExchange(message.getMessageProperties().getReceivedExchange());
			po.setDlxRouting(message.getMessageProperties().getReceivedRoutingKey());
			po.setDlxQueue(message.getMessageProperties().getConsumerQueue());
			po.setStatus(DlxStatusEnum.N.name());
			po.setDlxId(po.getDlxId() != null ? po.getDlxId() : UUID.randomUUID().toString());
			po.setOriExchange(message.getMessageProperties().getReceivedExchange());
			po.setOriRouting(message.getMessageProperties().getReceivedRoutingKey());
			po.setOriQueue(message.getMessageProperties().getConsumerQueue());
			po.setDlxType("AUTO");

			// 解析 x-death header
			if (xDeathObj instanceof List<?> xDeathList && !xDeathList.isEmpty()) {
				Map<String, Object> lastDeath = (Map<String, Object>) xDeathList.get(0);

				// 更新 DLX 資訊
				po.setDlxQueue((String) lastDeath.getOrDefault("queue", po.getDlxQueue()));
				po.setDlxExchange((String) lastDeath.getOrDefault("exchange", po.getDlxExchange()));

				Object routingKeys = lastDeath.get("routing-keys");
				if (routingKeys instanceof List<?> rkList && !rkList.isEmpty()) {
					po.setDlxRouting(String.join(",", rkList.stream().map(Object::toString).toList()));
				}

				// 組合錯誤原因
				StringBuilder errorDesc = new StringBuilder();
				if (lastDeath.get("queue") != null) {
					errorDesc.append("Queue: ").append(lastDeath.get("queue"));
				}
				if (lastDeath.get("reason") != null) {
					errorDesc.append(", Reason: ").append(lastDeath.get("reason"));
				}

				Object countObj = lastDeath.get("count");
				if (countObj instanceof Number number) {
					errorDesc.append(", Retry Count: ").append(number.longValue());
				}
				po.setErrorDesc(errorDesc.length() > 0 ? errorDesc.toString() : "Unknown reason");
			} else {
				po.setErrorDesc("No death information available");
				po.setDlxType("AUTO");
			}

			// payload 預設填入原始 body
			if (po.getPayload() == null) {
				po.setPayload(new String(message.getBody(), StandardCharsets.UTF_8));
			}

		} catch (Exception e) {
			log.error("解析 x-death header 發生錯誤 [messageId={}]", po.getMessageId(), e);
			po.setErrorDesc("Error parsing death information: " + e.getMessage());
		}
	}
}