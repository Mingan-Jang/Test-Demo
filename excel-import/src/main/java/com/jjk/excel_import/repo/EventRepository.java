package com.jjk.excel_import.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jjk.excel_import.po.EventPO;

@Repository
public interface EventRepository extends JpaRepository<EventPO, Long> {
}
