package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Refund;

public interface RefundRepository extends JpaRepository<Refund,Long>{
	
	@Query(value = "SELECT * FROM refunds WHERE action_id = ?1",nativeQuery = true)
	List<Refund> getAllRefundDetails(long aid);

}

