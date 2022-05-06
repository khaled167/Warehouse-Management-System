package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="actions")
@Data
@NoArgsConstructor
public class Action {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long action_id;

public Action(String action_type, String action_notes, Date action_date) {
	super();
	this.action_type = action_type;
	this.action_notes = action_notes;
	this.action_date = action_date;
}
private String action_type,action_notes;
private Date action_date;
}
