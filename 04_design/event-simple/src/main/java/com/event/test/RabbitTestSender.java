package com.event.test;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.event.config.RabbitMqConfig;
import com.event.dto.EventDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitTestSender implements ApplicationListener<ContextRefreshedEvent> {

	@Qualifier("rabbitTemplate")
	private final RabbitTemplate rb;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// TEST1
		// mandatoryRabbitTemplate.convertAndSend("exchangeError", "wrong.key", "測試消息");

		// Test2
		// CorrelationData cd = new CorrelationData();
		// cd.getFuture().orTimeout(5, TimeUnit.SECONDS).thenAccept(confirm -> {
		// System.out.println("Sent message: " + cd.getId());

		// if (confirm.isAck()) {
		// System.out.println("Message sent successfully!");
		// } else {
		// System.err.println("Message NOT sent! Reason: " + confirm.getReason());
		// }
		// }).exceptionally(ex -> {
		// System.err.println("Waiting for ACK/NACK timed out: " + ex.getMessage());
		// return null;
		// });

		EventDTO dto = new EventDTO();
		dto.setEventId(UUID.randomUUID().toString());
		dto.setOccurTime(LocalDateTime.parse("2025-09-14T14:30:00"));
		dto.setEventType("INCIDENT");
		dto.setPriority("HIGH");
		dto.setSoce("mq");
		dto.setDescription("月台異常警示");
		log.info("送出初始:{}", dto);

		rb.convertAndSend(RabbitMqConfig.PROCESS_DELAY_EXCHANGE, RabbitMqConfig.PROCESS_CREATE_ROUTING_KEY, dto);
	}
}