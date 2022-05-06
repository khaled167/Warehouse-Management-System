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
@Table(name="admin_actions")
@Data
@NoArgsConstructor
public class AdminAction {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long admin_action_id;
@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name="user_id")
private User user;
private String action_type,action_entity,action_on;
private Date action_date;
private long action_on_id;
public AdminAction(User user, String action_type, String action_entity,long action_on_id, String action_on, Date action_date) {
	super();
	this.user = user;
	this.action_type = action_type;
	this.action_entity = action_entity;
	this.action_on = action_on;
	this.action_date = action_date;
	this.action_on_id = action_on_id;
}





}
