package com.example.demo.entity;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="transactions")
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "transaction_id")
public class Transaction {
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
     private long transaction_id;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="action_id")
	private Action action;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="request_id")
	private Request request;
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="stock_id")
	private Stock stock;
	private double quantity;
	
	
	// RECEIVING WAREHOUSE ( TO WAREHOUSE )
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="warehouse_id")
	private Warehouse warehouse;
	
	
//	@OneToMany(mappedBy = "transaction")
//	@JsonInclude(JsonInclude.Include.NON_EMPTY)
//	private List<Refund> refunds;
	
	@Transient
	private int refundedQuantities;
	
	public Transaction(Action action, Request request, Stock stock, double quantity) {
		super();
		this.action = action;
		this.request = request;
		this.stock = stock;
		this.quantity = quantity;		
	}

//	public int getRefundedQuantities() {
//		int sum = 0;
//		for(Refund r : getRefunds())
//			sum += r.getRefund_quantity();
//		return sum;
//	}
}
