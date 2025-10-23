package com.jjk.env_test.springinit;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class MyService {
    
    @PostConstruct
    public void init() {
    	// 完成Bean填裝後，執行此方法
        System.out.println("MyService initialized");
    }
}
