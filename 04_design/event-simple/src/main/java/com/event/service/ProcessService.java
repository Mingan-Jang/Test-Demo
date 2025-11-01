package com.event.service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.event.config.RabbitMqConfig;
import com.event.dto.EventCompareDTO;
import com.event.dto.EventDTO;
import com.event.dto.FieldChangeDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ProcessService {

	private final RabbitTemplate rabbitTemplate;

	public void processCreate(EventDTO event) {
		EventCompareDTO compareDTO = new EventCompareDTO();
		compareDTO.setNewEvent(event);
		compareDTO.setOldEvent(null);

		compareDTO.setFieldChanges(null);
		publish(compareDTO);
	}

	public void processUpdate(EventDTO oldEvent, EventDTO event) {
		EventCompareDTO compareDTO = compare(oldEvent, event);
		publish(compareDTO);
	}

	private void publish(EventCompareDTO compareDTO) {
		try {
			rabbitTemplate.convertAndSend(RabbitMqConfig.BROADCAST_EXCHANGE, "", compareDTO);
			log.info("✅ 廣播事件成功: eventId = {}",
					compareDTO.getNewEvent() != null ? compareDTO.getNewEvent().getEventId() : "N/A");
		} catch (Exception e) {
			log.error("❌ 廣播事件失敗", e);
		}

	}

	public EventCompareDTO compare(EventDTO oldEvent, EventDTO newEvent) {
		EventCompareDTO compareDTO = new EventCompareDTO();
		compareDTO.setOldEvent(oldEvent);
		compareDTO.setNewEvent(newEvent);

		List<FieldChangeDTO> fieldChanges = new ArrayList<>();

		// UPDATE: 只比較不同的欄位
		for (Field field : newEvent.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				Object oldValue = field.get(oldEvent);
				Object newValue = field.get(newEvent);

				if (!Objects.equals(oldValue, newValue)) {
					FieldChangeDTO change = new FieldChangeDTO();
					change.setFieldName(field.getName());
					change.setOldValue(oldValue);
					change.setNewValue(newValue);
					change.setClassName(newValue != null ? newValue.getClass().getName()
							: oldValue != null ? oldValue.getClass().getName() : null);
					fieldChanges.add(change);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		compareDTO.setFieldChanges(fieldChanges);
		return compareDTO;

	}

	public void test() {

		EventDTO newEvent = new EventDTO();
		newEvent.setEventId("123");
		newEvent.setOccurTime(LocalDateTime.of(2025, 10, 26, 12, 0));
		newEvent.setEventType("INCIDENT");
		newEvent.setPriority("HIGH");
		newEvent.setDescription("月台異常警示");
		newEvent.setSoce("mq");

		System.out.println("===== CREATE 測試 =====");
		processCreate(newEvent);

		// 假資料 - UPDATE
		EventDTO oldEvent = new EventDTO();
		oldEvent.setEventId("123");
		oldEvent.setOccurTime(LocalDateTime.of(2025, 10, 26, 12, 0));
		oldEvent.setEventType("INCIDENT");
		oldEvent.setPriority("LOW"); // 舊值
		oldEvent.setDescription("月台異常警示");
		oldEvent.setSoce("mq");

		// 修改 newEvent 的部分欄位
		newEvent.setPriority("HIGH"); // 新值
		newEvent.setDescription("月台異常警示 - 更新"); // 新值

		System.out.println("===== UPDATE 測試 =====");
		EventCompareDTO compareDTO = compare(oldEvent, newEvent);

		// 列印 fieldChanges
		if (compareDTO.getFieldChanges() != null) {
			for (FieldChangeDTO change : compareDTO.getFieldChanges()) {
				System.out.println("欄位: " + change.getFieldName() + ", 舊值: " + change.getOldValue() + ", 新值: "
						+ change.getNewValue() + ", 類型: " + change.getClassName());
			}
		} else {
			System.out.println("沒有欄位變更");
		}
	}

}