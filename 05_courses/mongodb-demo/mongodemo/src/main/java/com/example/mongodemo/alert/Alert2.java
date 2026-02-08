package com.example.mongodemo.alert;

import java.util.Date;

class Person {
	String name;
	int age;
}

class Employee extends Person {
	String name; // 遮蔽父類欄位
}

public class Alert2 {
	public static void main(String[] args) {
		Employee e = new Employee();
		e.name = "Evie"; // 存取子類欄位
		e.age = 30; // 存取父類欄位
		System.out.println(e.name); // Evie → 子類欄位
		System.out.println(((Person) e).name); // null → 父類欄位
	}
}
