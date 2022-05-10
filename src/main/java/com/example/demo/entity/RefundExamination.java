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
@Data
@NoArgsConstructor
@Table(name = "refundexamination")
public class RefundExamination {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long refundexamination_id;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="examination_comittee_id")
	private ExaminationComittee examinationComittee;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="refund_id")
	private Refund refund;
	private String notes;

	@Column(name = "is_accepted")
	@JsonProperty("is_accepted")
	private boolean isAccepted;

	private double percentage_examined;
	public RefundExamination(ExaminationComittee examinationComittee, Refund refund,double percentage_examined, boolean isAccepted
			, String notes) {
		super();
		this.examinationComittee = examinationComittee;
		this.refund = refund;
		this.notes = notes;
		this.isAccepted = isAccepted;
		this.percentage_examined = percentage_examined;
	}
}
