package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Action;
import com.example.demo.entity.ExaminationCommittee;

public interface ExaminationCommitteeRepository extends JpaRepository<ExaminationCommittee, Long> {
	
	
	public List<ExaminationCommittee> findByActionIn(List<Action> list);
	
}
