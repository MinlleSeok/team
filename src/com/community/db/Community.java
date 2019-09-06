package com.community.db;

/*
 * community table DTO / VO / Bean 

create table community (
	num int primary key auto_increment,
	name varchar(30) not null,
	category varchar(30) not null
);

 */

public class Community {

	private int num;
	private String name;
	private String category;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
