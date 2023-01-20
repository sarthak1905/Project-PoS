package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
// TODO: Check on @Getter and @Setter of Lombok. You can avoid getters and setters.
public class EmployeePojo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
//	TODO: Use Boxed type for primitive datatypes -> can have "@column(nullable = false)" if it should not be null.
//	TODO: Also, in general, you can have "@column(nullable = false)" if it shouldn't be null.
	private int age;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
