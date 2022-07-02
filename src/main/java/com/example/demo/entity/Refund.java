package com.example.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="refunds")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property ="refund_id")
public class Refund {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long refund_id;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="transaction_id")
private Transaction transaction;
private double refund_quantity;
public Refund(Action action, com.example.demo.entity.Transaction transaction, double refund_quantity) {
	super();
	this.action = action;
	this.transaction = transaction;
	this.refund_quantity = refund_quantity;
}



}
