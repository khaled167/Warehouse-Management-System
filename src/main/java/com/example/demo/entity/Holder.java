package com.example.demo.entity;

import com.example.demo.repository.Tuple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor

public class Holder {
	Pair<Tuple> p;
	long warehouseId;

}
