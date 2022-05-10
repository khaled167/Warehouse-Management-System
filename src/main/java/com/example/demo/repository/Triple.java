package com.example.demo.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Triple {
	
	private long rid;
	private long sid;
	private double quantity;
	
}
