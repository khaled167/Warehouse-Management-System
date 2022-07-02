package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	@Column(name = "email_address")
	@JsonProperty("email_address")
	@Email
	private String emailAddress;
	@Column(name="date_of_birthday")
	private Date birthday;
	@Column(name="number_of_notification")
	private int NotificationNum;
	
	@Column(name = "is_available")
	@JsonProperty("is_available")
	private boolean isAvailable;
	
	@Column(name = "national_num")
	@JsonProperty("national_num")
	private String nationalNum;

	public User(String fullname, String username, String password, String address, String phoneNumber,
			String emailAddress, Date birthday, int notificationNum, boolean isAvailable, String nationalNum) {
		super();
		this.fullname = fullname;
		this.username = username;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.birthday = birthday;
		NotificationNum = notificationNum;
		this.isAvailable = isAvailable;
		this.nationalNum = nationalNum;
	}


	
	
}
