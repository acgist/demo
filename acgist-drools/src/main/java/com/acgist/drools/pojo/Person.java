package com.acgist.drools.pojo;

public class Person {

	private String name;
	private int age;
	private String desc;

	public Person() {
	}

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		final StringBuffer builder = new StringBuffer();
		builder.append("name").append("=").append(this.name).append("&");
		builder.append("age").append("=").append(this.age).append("&");
		builder.append("desc").append("=").append(this.desc);
		return builder.toString();
	}
	
}
