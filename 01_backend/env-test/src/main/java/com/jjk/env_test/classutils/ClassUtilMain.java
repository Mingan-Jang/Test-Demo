package com.jjk.env_test.classutils;

import org.springframework.util.ClassUtils;

public class ClassUtilMain {

	
	public static void main(String[] args) {
		
		System.out.println(ClassUtils.getUserClass(ClassUtilMain.class).getSimpleName());;
	
		System.out.println(ClassUtils.getUserClass(ClassUtilMainImpl.class).getSimpleName());;

		System.out.println(ClassUtils.getUserClass(ClassUtilMainInterface.class).getSimpleName());;

	}
}
