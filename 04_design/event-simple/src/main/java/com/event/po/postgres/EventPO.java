package com.event.po.postgres;

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
@Entity
@Table(name = "event")
@EntityListeners(AuditingEntityListener.class) // 啟用審計
public class EventPO {

    @Id
    @Column(name = "event_id", nullable = false, length = 36)
    private String eventId;

    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Column(name = "occur_time", nullable = false)
    private LocalDateTime occurTime;

    @Column(name = "event_type", length = 50)
    private String eventType;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "soce", length = 32)
    private String soce;

    @CreatedBy
    @Column(name = "creator", length = 50)
    private String creator;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @LastModifiedBy
    @Column(name = "updater", length = 50)
    private String updater;

    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}
