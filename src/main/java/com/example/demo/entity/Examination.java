package com.example.demo.entity;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="examinations")
@Data
@NoArgsConstructor
public class Examination {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long examination_id;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="examination_committee_id")
private ExaminationCommittee examinationCommittee;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="stock_id")
private Stock stock;
private String notes;

@Column(name = "is_accepted")
@JsonProperty("is_accepted")
private boolean isAccepted;

private double percentage_examined;

@Column(name = "quantity_examined")
@JsonProperty("quantity_examined")
private double quantityExamined;

public Examination(ExaminationCommittee examinationCommittee, Stock stock,double percentage_examined, boolean isAccepted
		, String notes) {
	super();
	this.examinationCommittee = examinationCommittee;
	this.stock = stock;
	this.notes = notes;
	this.isAccepted = isAccepted;
	this.percentage_examined = percentage_examined;
}



}
