package com.jjk.env_test.springinit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(prefix = "config.select", name = "test", havingValue = "ok")
public class ConditionBean {
	void executed(){
		System.out.println("ConditionBean executed");
	}
}
