package com.event.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.event.dto.EventDTO;
import com.event.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@Tag(name = "Event API", description = "事件管理相關 API")
@RestController("/event/api/v1")
@RequiredArgsConstructor
public class EventApiController {

	private final EventService eventService;

	// =======================
	// 1 建立事件
	// =======================
	@Operation(summary = "建立事件", description = "新增一筆事件資料")
	@PostMapping
	public ResponseEntity<EventDTO> create(@RequestBody EventDTO dto) {
		EventDTO created = eventService.createEvent(dto);
		return ResponseEntity.ok(created);
	}

	// =======================
	// 2 更新事件
	// =======================
	@Operation(summary = "更新事件", description = "依事件 ID 更新事件資料")
	@PutMapping("/{eventId}")
	public ResponseEntity<EventDTO> update(
			@Parameter(description = "事件 ID", example = "c4f5e9d0-1234-5678-9012-abcdef123456") @PathVariable String eventId,
			@RequestBody EventDTO dto) {
		EventDTO updated = eventService.updateEvent(eventId, dto);
		return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
	}

}
