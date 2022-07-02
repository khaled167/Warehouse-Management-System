package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.Refund;
import com.example.demo.entity.RefundExamination;

@Repository
public interface RefundExaminationRepository extends JpaRepository<RefundExamination, Long> {

	public List<RefundExamination> findByRefund(Refund refund);
	public List<RefundExamination> findByExaminationCommittee(ExaminationCommittee ec);
	
	public List<RefundExamination> findByRefundIn(List<Refund> list);
	
}
