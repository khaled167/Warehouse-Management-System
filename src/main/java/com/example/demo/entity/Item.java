package com.example.demo.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	private String item_name;
	
	private String category;

	private String unit;
	
	private String description;
	
	public boolean isIs_available() {
		return is_available;
	}

	public void setIs_available(boolean is_available) {
		this.is_available = is_available;
	}

	public boolean isIs_consumable() {
		return is_consumable;
	}

	public void setIs_consumable(boolean is_consumable) {
		this.is_consumable = is_consumable;
	}

	private boolean is_available;
	
	private boolean is_consumable;
	

}
