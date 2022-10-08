package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;
import com.example.demo.entity.ItemMonthlyRequest;

public interface ItemMonthlyRequestRepository extends JpaRepository<ItemMonthlyRequest, Long>{
	
	
	ItemMonthlyRequest findByItemAndMonthNumberAndYearNumber(Item item,int month,int year);
	
	List<ItemMonthlyRequest> findByItemAndMonthNumber(Item item,int month);

	
}
