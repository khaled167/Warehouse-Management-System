package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="examination_member_signatures")
@Data
@NoArgsConstructor
public class ExaminationMemberSignature {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long examination_member_sign_id;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="examination_committee_id")
private ExaminationCommittee examinationCommittee;
public ExaminationMemberSignature(ExaminationCommittee examinationCommittee,
		ExaminationMember examinationMember, Date examination_date) {
	super();
	this.examinationCommittee = examinationCommittee;
	this.examinationMember = examinationMember;
	this.examination_date = examination_date;
}
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="examination_member_id")
private ExaminationMember examinationMember;
private Date examination_date;

}
