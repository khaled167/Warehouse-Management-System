package com.example.demo.repository;

import lombok.Data;

@Data
public class RequestHolder {
	long itemId;
	String exchange_reason,notes;
	double requested_quantity;
}
