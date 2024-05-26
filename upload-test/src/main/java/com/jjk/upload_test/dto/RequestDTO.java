package com.jjk.upload_test.dto;

import java.util.Date;
import java.util.TimeZone;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RequestDTO {
	@Size(min=3)
	String name;
	String age;
	
    // 傳來的時間是 標準時間
	Date dateInDefaultFormat;
	
    // 傳來的時間被當作GTC+8的時間，轉換成系統時間 +(8-8) 
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH")
	Date dateInJSONFormatGMT08;
    
    // 傳來的時間被當作GTC+2的時間，轉換成系統時間 +(8-2)
    @JsonFormat(  timezone = "GMT+2", pattern = "yyyy-MM-dd'T'HH")
 	Date dateInJSONFormatGMT02;
     
    // 傳來的時間被當作GTC+0，轉換成系統時間 +(8)
    @JsonFormat(timezone = JsonFormat.DEFAULT_TIMEZONE ,pattern = "yyyy-MM-dd'T'HH")
	Date dateInJSONFormatInDEFAULT_TIMEZONE;
    
    
     @JsonFormat(pattern = "yyyy-MM-dd'T'HH")
     // 傳來的時間是標準時間，轉換標準時間
 	Date dateInJSONFormat;
     
     @DateTimeFormat(iso = ISO.NONE)
     Date dateTimeFormatNone;
    
    @DateTimeFormat(iso = ISO.DATE_TIME)
    Date dateTimeFormatDateTime;
    
    @DateTimeFormat(iso = ISO.DATE)
    Date dateTimeFormatDate;
    
    @DateTimeFormat(iso = ISO.TIME)
    Date dateTimeFormatTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    Date dateSSX;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    Date dateSSZ;
}
