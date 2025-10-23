package com.jjk.demo;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TestComp {

	@PostConstruct
	public void print() {
		System.out.println("ASASAS");
	}
}
