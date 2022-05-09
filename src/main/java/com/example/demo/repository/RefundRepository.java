package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Action;
import com.example.demo.entity.Refund;

public interface RefundRepository extends JpaRepository<Refund,Long>{
		
	List<Refund> findByAction(Action action);

}

