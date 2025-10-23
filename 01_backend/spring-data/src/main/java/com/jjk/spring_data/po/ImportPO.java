package com.jjk.spring_data.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "spring_data_demo")
public class ImportPO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "the_name", length = 255, nullable = false)
	private String name;

	@Column(name = "the_age", length = 10)
	private Integer age;

	@Column(name = "the_job", length = 255)
	private String job;

	public ImportPO(String name, int age) {
		this.name = name + " Kai";
		this.age = age;
	}
}
