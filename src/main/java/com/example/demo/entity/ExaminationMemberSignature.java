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
@JoinColumn(name="examination_comittee_id")
private ExaminationComittee examinationComittee;
public ExaminationMemberSignature(com.example.demo.entity.ExaminationComittee examinationComittee,
		ExaminationMember examinationComittee2, Date examination_date) {
	super();
	this.examinationComittee = examinationComittee;
	ExaminationComittee = examinationComittee2;
	this.examination_date = examination_date;
}
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="examination_member_id")
private ExaminationMember ExaminationComittee;
private Date examination_date;

}
