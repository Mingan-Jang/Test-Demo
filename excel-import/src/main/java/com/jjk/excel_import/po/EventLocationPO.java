package com.jjk.excel_import.po;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "rb_event_location")
public class EventLocationPO {
  
  @Id
  private String eventOid;

  private double lat;

  private double lng;

  private String lineId;

  private String originStation;

  private String destinationStation;

  private String trainNo;

  private String otherLocation;
}
