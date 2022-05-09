package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "stocks")
@Data
@NoArgsConstructor
public class Stock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_id")
	@JsonProperty("stock_id")
	private long id;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "warehouse_id")
	private Warehouse warehouse;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "item_id")
	private Item item;
	
	@Column(name = "entry_date")
	@JsonProperty("entry_date")
	private Date entryDate;
	
	@Column(name = "expired_date")
	@JsonProperty("expired_date")
	private Date expiredDate;
	
	
	private double price;
	private double quantity;
	private String status;

	public void clone(Stock org) {
		this.item = org.getItem();
		this.entryDate = org.getEntryDate();
		this.expiredDate = org.getExpiredDate();
		this.price = org.getPrice();
		this.status = org.getStatus();
	}

}
