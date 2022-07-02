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
@Table(name="examination_committees")
@Data
@NoArgsConstructor
public class ExaminationCommittee {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long examination_committee_id;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
private int decision_number,supply_number;
private Date supply_date;
private String head_name;
}
