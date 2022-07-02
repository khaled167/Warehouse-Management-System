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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="signatures")
@Data
@NoArgsConstructor
public class Signature {
@Id
@GeneratedValue(strategy =GenerationType.IDENTITY)
private long signature_id; 
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="role_id")
private Role role;
public Signature(Action action, Role role, Date seen_date, Date submitDate) {
	super();
	this.action = action;
	this.role = role;
	this.seen_date = seen_date;
	this.submitDate = submitDate;
}
private Date seen_date;
@JsonProperty("submit_date")
private Date submitDate;
}

