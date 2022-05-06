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
@Table(name="examinations")
@Data
@NoArgsConstructor
public class Examination {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long examination_id;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="examination_comittee_id")
private ExaminationComittee examinationComittee;
@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
@JoinColumn(name="stock_id")
private Stock stock;
private String notes;
private boolean is_accepted;
private double percentage_examined;
public boolean isIs_accepted() {
	return is_accepted ;
}
public void setIs_accepted(boolean is_accepted) {
	this.is_accepted = is_accepted;
}

}
