package com.event.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.event.dao.clickhouse.EventHistoryDAO;
import com.event.dao.postgres.EventDAO;
import com.event.po.clickhouse.EventHistoryPO;
import com.event.po.postgres.EventPO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AllEventService {

	private final EventHistoryDAO eventHistoryDAO;
	private final EventDAO eventDAO;

	public List<EventPO> findByOccurTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
		List<EventPO> currentEvents = eventDAO.findByOccurTimeBetween(startTime, endTime);
		List<EventHistoryPO> historyEvents = eventHistoryDAO.findByOccurTimeBetween(startTime, endTime);

		Map<String, EventPO> mergedMap = new LinkedHashMap<>();

		// 先放歷史事件
		for (EventHistoryPO history : historyEvents) {
			EventPO converted = convertHistoryToEvent(history);
			mergedMap.put(converted.getEventId(), converted);
		}

		// 再放即時事件（若重複 eventId，會覆蓋歷史事件）
		for (EventPO current : currentEvents) {
			mergedMap.put(current.getEventId(), current);
		}

		List<EventPO> merged = new ArrayList<>(mergedMap.values());

		log.info("查詢發生時間介於 {} 至 {} 的事件：目前事件 {} 筆，歷史事件 {} 筆，去重後合計 {} 筆", startTime, endTime, currentEvents.size(),
				historyEvents.size(), merged.size());

		return merged;
	}

	public List<EventPO> findAllEvents() {
		List<EventPO> currentEvents = eventDAO.findAll();
		List<EventHistoryPO> historyEvents = eventHistoryDAO.findAll();

		// 轉型：若你希望回傳統一型別，可將 EventHistoryPO 轉成 EventPO
		List<EventPO> merged = new ArrayList<>(currentEvents);
		merged.addAll(historyEvents.stream().map(this::convertHistoryToEvent).toList());

		log.info("合併事件查詢完成：目前事件 {} 筆，歷史事件 {} 筆，合計 {} 筆", currentEvents.size(), historyEvents.size(), merged.size());

		return merged;
	}

	private EventPO convertHistoryToEvent(EventHistoryPO history) {
		EventPO event = new EventPO();
		event.setEventId(history.getEventId());
		event.setEventType(history.getEventType());
		event.setPriority(history.getPriority());
		event.setDescription(history.getDescription());
		event.setSoce(history.getSoce());
		event.setMessageId(history.getMessageId());
		event.setOccurTime(history.getOccurTime());
		event.setCreator(history.getCreator());
		event.setCreateTime(history.getCreateTime());
		event.setUpdater(history.getUpdater());
		event.setUpdateTime(history.getUpdateTime());
		return event;
	}

}