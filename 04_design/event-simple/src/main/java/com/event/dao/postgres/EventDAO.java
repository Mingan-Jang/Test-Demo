package com.event.dao.postgres;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.event.po.postgres.EventPO;

@Repository
public interface EventDAO extends JpaRepository<EventPO, String> {

	List<EventPO> findByEventType(String eventType);

	Optional<EventPO> findByMessageId(String messageId);

	List<EventPO> findByOccurTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

}
