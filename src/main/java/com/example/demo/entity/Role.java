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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	 @Id
	  @GeneratedValue(strategy=GenerationType.IDENTITY)
		private long role_id;
	 @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	 @JoinColumn(name="user_id")
	    private User user;
	 @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	 @JoinColumn(name="warehouse_id")
	    private Warehouse warehouse;
		private double salary;
		private String role;
		private Date date_of_assign;
		private Date date_of_resign;
		public Role(User user, Warehouse warehouse, double salary, String role, Date date_of_assign,
				Date date_of_resign) {
			super();
			this.user = user;
			this.warehouse = warehouse;
			this.salary = salary;
			this.role = role;
			this.date_of_assign = date_of_assign;
			this.date_of_resign = date_of_resign;
		}

		

}
