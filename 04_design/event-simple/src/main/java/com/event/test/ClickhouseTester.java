package com.event.test;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.event.dao.clickhouse.EventHistoryDAO;
import com.event.po.clickhouse.EventHistoryPO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClickhouseTester implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private final EventHistoryDAO dao;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// 1. 創建假資料物件
		EventHistoryPO testEvent = new EventHistoryPO();
		LocalDateTime now = LocalDateTime.now();

		// 2. 填充假資料
		testEvent.setEventId(UUID.randomUUID().toString());
		testEvent.setEventType("SYSTEM_ALERT");
		testEvent.setPriority("HIGH");
		testEvent.setDescription("ClickHouse connection test successful upon context refresh.");
		testEvent.setSoce("APP_SERVER_01");
		testEvent.setMessageId("MSG-20251026-0001");
		testEvent.setOccurTime(now.minusMinutes(5));
		testEvent.setCreateTime(now);
		testEvent.setUpdateTime(now);
		testEvent.setCreator("SystemStartup");
		testEvent.setUpdater("SystemStartup");

		log.info("Attempting to save test EventHistoryPO to ClickHouse: {}", testEvent.getEventId());

		// 3. 呼叫 DAO 的 save 方法
		dao.save(testEvent);

		log.info("Test EventHistoryPO saved successfully.");
	}

}