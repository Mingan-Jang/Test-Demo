package com.jjk.excel_import.po;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "rb_event")
@EntityListeners(AuditingEntityListener.class)
public class EventPO {

	private String oid;

	private Integer version;

	private String id;

	private String operator;

	private String operatorEventId;

	private String categoryId;

	private String otherCategory;

	private Integer status;

	private String comment;

	private Integer disasterScale;

	private String siteStatusDesc;

	private String equipStatusDesc;

	private String competentAuthority;

	private String competentAuthorityTel;

	private String supportedOrg;

	private String supportedDesc;

	private String notifyOrg;

	private String notifyTitle;

	private String notifyName;

	private String notifyTel;

	private String notifyFax;

	private String cmdrOrg;

	private String cmdrTitle;

	private String cmdrName;

	private String cmdrTel;

	private Instant ertCreateTime;

	private Instant ertReleaseTime;

	private String ertDesc;

	private Integer casualtyStatus;

	private String casualtyDesc;

	@Column(name = "operating_status_interruption_desc")
	private String operatingStatusInterruptionDesc;

	@Column(name = "operating_status_cancel_desc")
	private String operatingStatusCancelDesc;

	@Column(name = "operating_status_alteration_desc")
	private String operatingStatusAlterationDesc;

	@Column(name = "operating_status_delayed60_desc")
	private String operatingStatusDelayed60Desc;

	@Column(name = "operating_status_delayed3060_desc")
	private String operatingStatusDelayed3060Desc;

	@Column(name = "operating_status_delayed0530_desc")
	private String operatingStatusDelayed0530Desc;

	@Column(name = "operating_status_addition_desc")
	private String operatingStatusAdditionDesc;

	@Column(name = "operating_status_other_desc")
	private String operatingStatusOtherDesc;

	@Column(name = "know_alteration_flag")
	private boolean knowAlterationFlag;

	@Column(name = "know_alteration_3days_flag")
	private boolean knowAlteration3daysFlag;

	private boolean tempFlag;

	private String occurDesc;

	private Instant occurTime;

	private Instant endTime;

	private Instant notifyTime;

	@CreatedDate
	private Instant createTime;

	@CreatedBy
	private String createBy;

	private boolean deleteFlag;

	@Column(name = "status_version")
	private Integer statusVersion;

	@Size(max = 2000)
	@Column(name = "improvement_suggestion", length = 2000)
	private String improvementSuggestion;

	@Column(name = "extended_info_id")
	private String extendedInfoId;
}