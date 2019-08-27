package com.example.psh.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Parameter implements Serializable {
	
	private String key;
	private String value;
	
	public String getKey() {
		return this.key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
