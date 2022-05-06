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
@Table(name="signatures")
@Data
@NoArgsConstructor
public class Signature {
@Id
@GeneratedValue(strategy =GenerationType.IDENTITY)
private long signature_id; 
@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name="user_id")
private User user;
public Signature(Action action, User user, Date seen_date, Date submit_date) {
	super();
	this.action = action;
	this.user = user;
	this.seen_date = seen_date;
	this.submit_date = submit_date;
}
private Date seen_date,submit_date;
}
