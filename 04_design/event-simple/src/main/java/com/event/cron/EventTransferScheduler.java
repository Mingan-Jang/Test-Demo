package com.event.cron;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.event.dao.postgres.EventDAO;
import com.event.po.postgres.EventPO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventTransferScheduler {

	private final EventDAO eventDAO;

	@Scheduled(cron = "0 0 2 * * ?")
	public void archiveOldEvents() {
		LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
		log.info("開始蒐集 2 年前的事件 [createTime < {}]", twoYearsAgo);

		List<EventPO> oldEvents = eventDAO.findAll().stream()
				.filter(e -> e.getCreateTime() != null && e.getCreateTime().isBefore(twoYearsAgo)).toList();

		log.info("蒐集到 {} 筆舊事件", oldEvents.size());
	
		

	}
}