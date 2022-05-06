package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
	
	@Query(value = "SELECT * FROM transactions WHERE action_id = ?1",nativeQuery = true)
	List<Transaction> getAllTransactionDetails(long aid);

}
