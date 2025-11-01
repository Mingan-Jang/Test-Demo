package com.event.service;

import java.time.LocalDateTime;

import com.event.dto.EventDTO;
import com.event.exception.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PreProcessService {

	protected void validateEvent(EventDTO event) throws ValidationException {
		if (event == null) {
			throw new ValidationException("事件不能為空");
		}

		if (event.getEventId() == null || event.getEventId().trim().isEmpty()) {
			throw new ValidationException("EventId 不能為空");
		}

		if (event.getEventType() == null || event.getEventType().trim().isEmpty()) {
			throw new ValidationException("EventType 不能為空");
		}

		if (event.getOccurTime() == null) {
			throw new ValidationException("OccurTime 不能為空");
		}

		LocalDateTime now = LocalDateTime.now();
		if (event.getOccurTime().isAfter(now)) {
			throw new ValidationException("事件發生時間不能晚於現在");
		}

	}

}
