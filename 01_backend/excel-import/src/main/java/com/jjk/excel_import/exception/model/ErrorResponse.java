package com.jjk.excel_import.exception.model;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
  
  private String code;

  private String message;

  private ZonedDateTime timestamp;

}
