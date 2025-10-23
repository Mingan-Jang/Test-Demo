package com.jjk.excel_import.po;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class EventCasualtyDetailPK implements Serializable {
  
  private String eventOid;

  private String type;

}
