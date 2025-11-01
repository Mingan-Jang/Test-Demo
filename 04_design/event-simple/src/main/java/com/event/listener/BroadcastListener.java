package com.event.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.event.config.RabbitMqConfig;
import com.event.dto.EventCompareDTO;
import com.event.service.BroadcastService;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BroadcastListener {

	private final BroadcastService broadcastService;

	@RabbitListener(queues = RabbitMqConfig.BROADCAST_QUEUE, ackMode = "MANUAL", concurrency = "3-5")
	public void handleBroadcastEvent(EventCompareDTO eventCompare, Message amqpMessage, Channel channel,
			@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws Exception {
		broadcastService.handleBroadcastEvent(eventCompare, amqpMessage, channel, deliveryTag);
	}
}
