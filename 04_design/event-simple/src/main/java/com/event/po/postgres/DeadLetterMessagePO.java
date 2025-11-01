package com.event.po.postgres;

import java.time.LocalDateTime;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "dead_letter_message")
@EntityListeners(AuditingEntityListener.class)
public class DeadLetterMessagePO {

	@Id
	@Column(name = "message_id", length = 100, nullable = false)
	private String messageId;

	@Column(name = "dlx_id", length = 36, nullable = false)
	private String dlxId;

	@Column(name = "dlx_exchange", length = 64)
	private String dlxExchange;

	@Column(name = "dlx_routing", length = 64)
	private String dlxRouting;

	@Column(name = "dlx_queue", length = 64)
	private String dlxQueue;

	@Type(JsonBinaryType.class)
	@Column(name = "payload", nullable = false, columnDefinition = "jsonb")
	private String payload;

	@Column(name = "error_type", length = 256)
	private String errorType;

	@Column(name = "error_desc", length = 256)
	private String errorDesc;

	@Column(name = "status", length = 50)
	private String status = "NEW";

	@Column(name = "ori_exchange", length = 64)
	private String oriExchange;

	@Column(name = "ori_routing", length = 64)
	private String oriRouting;

	@Column(name = "ori_queue", length = 64)
	private String oriQueue;

	@Column(name = "dlx_type", length = 20)
	private String dlxType;

	@CreatedBy
	@Column(name = "creator", length = 50)
	private String creator;

	@CreatedDate
	@Column(name = "create_time", nullable = false)
	private LocalDateTime createTime;

}