package com.event.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.event.config.RabbitMqConfig;
import com.event.dto.EventDTO;
import com.event.service.EventMqService;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventMqListener {

	private final EventMqService eventMqService;

	@RabbitListener(queues = RabbitMqConfig.PROCESS_DELAY_QUEUE, ackMode = "MANUAL", concurrency = "3-5")
	public void handleExternalData(EventDTO event, Message amqpMessage, Channel channel,
			@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws Exception {
		eventMqService.handleEvent(event, amqpMessage, channel, deliveryTag);
	}

}
