package com.example.demo.entity;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationParam <T> {
	private String notes;
	private ExaminationComittee ec;
	private List<T> list;
	private List<ExaminationMember> exmem;
	
}
