package com.event.po.clickhouse;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class EventHistoryPO {

	private String eventId;
	private String eventType;
	private String priority;
	private String description;
	private String soce;
	private String messageId;
	private LocalDateTime occurTime;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private String creator;
	private String updater;

}
