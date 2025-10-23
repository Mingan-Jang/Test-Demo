package com.jjk.excel_import.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "rb_event_casualty_detail")
@IdClass(EventCasualtyDetailPK.class)
public class EventCasualtyDetailPO {

  @Id
  private String eventOid;

  @Id
  private String type;

  private Integer deaths;

  private Integer injuries;

  private Integer missing;

  private String comment;
  
}
