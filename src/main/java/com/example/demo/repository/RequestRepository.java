package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Request;

public interface RequestRepository extends JpaRepository<Request,Long>{

	@Query(value = "SELECT * FROM requests WHERE action_id = ?1",nativeQuery = true)
	public List<Request> getAllRequestDetails(long aid);

	
}

