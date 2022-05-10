package com.example.demo.repository;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Tuple {
	@JsonProperty("id")
	private long id;
	@JsonProperty("value")
	private int value;
}
