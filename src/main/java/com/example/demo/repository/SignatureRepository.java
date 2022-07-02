package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Action;
import com.example.demo.entity.Signature;

@Repository
public interface SignatureRepository extends JpaRepository<Signature,Long> {
	List<Signature> findByAction(Action action);
	
	// select role.warehouse_id from signature where action_id = actionId order by submit_date asc
	
	Signature findTopByActionOrderBySubmitDateAsc(Action action);
	
	
}
