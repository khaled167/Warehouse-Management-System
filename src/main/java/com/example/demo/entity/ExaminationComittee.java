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
@Table(name="examination_comittees")
@Data
@NoArgsConstructor
public class ExaminationComittee {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long examination_comittee_id;
@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
private int decision_number,supply_number;
private Date supply_date;
private String examination_type,head_name;

}
