package com.example.demo.repository;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Quadra  {

	long id;
	double percentage;
	
	@JsonProperty("is_accepted")
	boolean isAccepted;
	
	String notes;
	
}
