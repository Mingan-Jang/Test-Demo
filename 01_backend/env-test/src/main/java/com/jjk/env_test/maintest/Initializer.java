package com.jjk.env_test.maintest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class Initializer {

	@Autowired
	private RecursiveService recursiveService;

	@PostConstruct
	public void init() {
		recursiveService.recursiveMethod(0); // 通过代理对象调用
	}
}