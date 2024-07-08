package com.jjk.env_test.maintest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TetsMD5 {
	 public static void main(String[] args) {
	        Instant now = Instant.now();
	        
	        // 正确的方式：将 Instant 转换为带有时区信息的 ZonedDateTime
	        ZonedDateTime zdt = ZonedDateTime.ofInstant(now, ZoneId.of("Asia/Taipei"));
	        
	        System.out.println("当前时间 (台北时区): " + zdt);
	        
	        
	        Instant now2 = Instant.now();
	        LocalDateTime ldt = LocalDateTime.ofInstant(now2, ZoneId.of("Asia/Taipei"));
	        System.out.println("当前时间 (台北时区): " + ldt);
	    }
}
