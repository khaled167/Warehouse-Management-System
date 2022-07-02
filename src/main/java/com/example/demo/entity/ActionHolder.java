package com.example.demo.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionHolder {
	long action_id;
	String action_note;
	Date action_date;
	int progress;
	String warehouse_name,last_user;
}
