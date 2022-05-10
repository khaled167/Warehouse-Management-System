package com.example.demo.entity;

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
@Table(name="requests")
@Data
@NoArgsConstructor
public class Request {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long request_id;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="item_id")
private Item item;
private String exchange_reason,notes;
private double allowed_quantity,requested_quantity;
public Request(Action action, Item item, String exchange_reason, String notes, double allowed_quantity,
		double requested_quantity) {
	super();
	this.action = action;
	this.item = item;
	this.exchange_reason = exchange_reason;
	this.notes = notes;
	this.allowed_quantity = allowed_quantity;
	this.requested_quantity = requested_quantity;
}



}
