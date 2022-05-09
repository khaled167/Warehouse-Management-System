package com.example.demo.entity;

import java.sql.Date;

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
@Table(name="warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
	  @Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
		private long warehouse_id;
	  
		@Column(name = "warehouse_name")
		@JsonProperty("warehouse_name")
		private String warehouseName;
		
		private String location;
		
		@Column(name = "phone_number")
		@JsonProperty("phone_number")
		private String phoneNumber;
		
		@Column(name = "warehouse_type")
		@JsonProperty("warehouse_type")
		private String warehouseType;
		
		private String description;
		
		@Column(name = "establishment_time")
		@JsonProperty("establishment_time")
		private Date establishmentTime;
		
		@Column(name = "is_available")
		@JsonProperty("is_available")
		private boolean isAvailable;

		public Warehouse(String warehouseName, String location, String phoneNumber, String warehouseType,
				String description, Date establishmentTime, boolean isAvailable) {
			super();
			this.warehouseName = warehouseName;
			this.location = location;
			this.phoneNumber = phoneNumber;
			this.warehouseType = warehouseType;
			this.description = description;
			this.establishmentTime = establishmentTime;
			this.isAvailable = isAvailable;
		}


		
}
