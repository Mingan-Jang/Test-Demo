package com.event.dao.postgres;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.event.po.postgres.DeadLetterMessagePO;

@Repository
public interface DeadLetterMessageDAO extends JpaRepository<DeadLetterMessagePO, String> {

    DeadLetterMessagePO findByMessageId(String messageId);

}