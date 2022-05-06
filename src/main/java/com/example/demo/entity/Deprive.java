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
@Table(name="deprives")
@Data
@NoArgsConstructor
public class Deprive {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long deprive_id;
@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name="action_id")
private Action action;
@ManyToOne(cascade = {CascadeType.ALL,CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
@JoinColumn(name="stock_id")
private Stock stock;
private double real_quantity,increased_quantity,decreased_quantity;
private String status; 


}
