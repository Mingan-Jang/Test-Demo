package com.event.service;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import com.event.dto.EventCompareDTO;
import com.event.dto.EventDTO;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BroadcastService {

	private final EventService eventService;

	public void handleBroadcastEvent(EventCompareDTO eventCompare, Message amqpMessage, Channel channel,
			long deliveryTag) throws Exception {
		try {
			EventDTO newEvent = eventCompare.getNewEvent();
			EventDTO oldEvent = eventCompare.getOldEvent();

			if (oldEvent == null) {
				// CREATE
				eventService.createEvent(newEvent);
			} else {
				// UPDATE
				eventService.updateEvent(eventCompare.getNewEvent().getEventId(), eventCompare.getNewEvent());
			}

			// 處理完成後 ACK
			channel.basicAck(deliveryTag, false);
		} catch (Exception e) {
			log.error("處理廣播事件失敗 - eventId: {}，原因: {}",
					eventCompare.getNewEvent() != null ? eventCompare.getNewEvent().getEventId() : "N/A",
					e.getMessage(), e);

			// 失敗時 NACK，並可以選擇不重新入隊 (requeue=false)
			channel.basicNack(deliveryTag, false, false);
		}
	}

}