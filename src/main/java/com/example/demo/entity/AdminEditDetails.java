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
@Table(name="admin_edit_details")
@Data
@NoArgsConstructor

public class AdminEditDetails {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long admin_edit_details_id;
	@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="admin_action_id")
	private AdminAction AdminAction;
	private String edit_type,old_value;
	public AdminEditDetails(AdminAction adminAction, String edit_type, String old_value) {
		super();
		AdminAction = adminAction;
		this.edit_type = edit_type;
		this.old_value = old_value;
	}
	
}
