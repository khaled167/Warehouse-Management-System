package com.example.demo.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExaminationParam {
	private String ex_type,notes;
	private ExaminationComittee ec;
	private List<Examination> list;
	private List<ExaminationMember> exmem;
	
}
