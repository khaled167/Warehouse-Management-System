package com.example.demo.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="examination_members")
@Data
@NoArgsConstructor
public class ExaminationMember {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long examination_member_id;
private String member_name,member_job;


}
