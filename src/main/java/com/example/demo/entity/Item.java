package com.example.demo.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long item_id;
	
	
	@Column(name = "item_name")
	@JsonProperty("item_name")
	private String itemName;
	private String category;

	private String unit;
	
	private String description;

	@Column(name = "is_available")
	@JsonProperty("is_available")
	private boolean isAvailable;
	@Column(name = "is_consumable")
	@JsonProperty("is_consumable")
	private boolean isConsumable;
	

}
