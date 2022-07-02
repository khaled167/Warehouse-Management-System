package com.example.demo.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="actions")
@Data
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "action_id")
public class Action {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long action_id;

@Column(name = "action_type")
@JsonProperty("action_type")
private String actionType;

@Column(name = "action_notes")
@JsonProperty("action_notes")
private String actionNotes;

@Column(name = "action_date")
@JsonProperty("action_date")
private Date actionDate;


//@OneToMany(mappedBy = "action")
//@JsonManagedReference
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
//private List<Request> requests;


@Transient
private long averageProgress;


public Action(String actionType, String actionNotes, Date actionDate) {
	super();
	this.actionType = actionType;
	this.actionNotes = actionNotes;
	this.actionDate = actionDate;
}

//@JsonManagedReference
//public List<Request> getRequests(){return this.requests;}
//
//public long getAverageProgress() {
//	long sum = 0;
//	for(Request req : getRequests()) 
//		sum += req.getReceived_quantity();
//	return sum;
//}

}
