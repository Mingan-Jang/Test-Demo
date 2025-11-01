package com.event.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.event.po.postgres.EventPO;
import com.event.service.AllEventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "All-Event API",description = "全事件管理相關 API")
@RestController("/all-event/api/v1")
@RequiredArgsConstructor
public class AllEventApiController {

	private final AllEventService allEventService;

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	// =======================
	// 查詢事件 by 發生時間區間（歷史 + 即時）
	// =======================
	@Operation(summary = "查詢事件 by 時間區間", description = "依發生時間區間查詢事件（含歷史與即時事件）")
	@GetMapping("/search")
	public ResponseEntity<List<EventPO>> searchByOccurTime(
			@Parameter(description = "開始時間 yyyy-MM-dd'T'HH:mm:ss", example = "2025-11-01T00:00:00") @RequestParam String start,
			@Parameter(description = "結束時間 yyyy-MM-dd'T'HH:mm:ss", example = "2025-11-01T23:59:59") @RequestParam String end) {

		LocalDateTime startTime = LocalDateTime.parse(start, dateTimeFormatter);
		LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);

		List<EventPO> events = allEventService.findByOccurTimeRange(startTime, endTime);
		return ResponseEntity.ok(events);
	}
}
