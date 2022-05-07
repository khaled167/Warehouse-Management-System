package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long user_id;
	private String fullname,username,password,address;
	@Column(name="phone_number")
	private String phoneNumber;
	private String email_address;
	@Column(name="date_of_birthday")
	private Date birthday;
	@Column(name="number_of_notification")
	private int NotificationNum;
	private boolean is_available;
	private String national_num;
	public User(String fullname, String username, String password, String address, String phoneNumber,
			String email_address, Date birthday, int notificationNum, boolean is_available, String national_num) {
		super();
		this.fullname = fullname;
		this.username = username;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email_address = email_address;
		this.birthday = birthday;
		NotificationNum = notificationNum;
		this.is_available = is_available;
		this.national_num = national_num;
	}

	


	

	
	
}
