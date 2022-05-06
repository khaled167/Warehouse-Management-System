package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
	  @Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
		private long warehouse_id;
		private String warehouse_name,location,phone_number,warehouse_type,description;
		private Date establishment_time;
		private boolean is_available;
		public boolean isIs_available() {
			return is_available;
		}
		public void setIs_available(boolean is_available) {
			this.is_available = is_available;
		}
}
