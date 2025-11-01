package com.event.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.event.dao.postgres.EventDAO;
import com.event.dto.EventDTO;
import com.event.mapper.EventMapper;
import com.event.po.postgres.EventPO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventDAO eventDAO;

    @Transactional(readOnly = true)
    public EventDTO findByMessageId(String messageId) {
        return eventDAO.findByMessageId(messageId)
                .map(EventMapper.INSTANCE::toDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public EventDTO getEvent(String eventId) {
        return eventDAO.findById(eventId)
                .map(EventMapper.INSTANCE::toDTO)
                .orElse(null);
    }

    @Transactional
    public EventDTO createEvent(EventDTO dto) {
        dto.setEventId(UUID.randomUUID().toString());
        EventPO po = EventMapper.INSTANCE.toEntity(dto);
        po = eventDAO.save(po);

        log.info("Event created: {}", po.getEventId());
        return EventMapper.INSTANCE.toDTO(po);
    }

    @Transactional
    public EventDTO updateEvent(String eventId, EventDTO dto) {
        Optional<EventPO> existingPoOpt = eventDAO.findById(eventId);
        if (existingPoOpt.isEmpty()) {
            return null;
        }

        // 保留 eventId
        dto.setEventId(eventId);
        EventPO po = EventMapper.INSTANCE.toEntity(dto);
        po = eventDAO.save(po);

        log.info("Event updated: {}", po.getEventId());
        return EventMapper.INSTANCE.toDTO(po);
    }

    @Transactional
    public void deleteEvent(String eventId) {
        if (eventDAO.existsById(eventId)) {
            eventDAO.deleteById(eventId);
            log.info("Event deleted: {}", eventId);
        }
    }

}
