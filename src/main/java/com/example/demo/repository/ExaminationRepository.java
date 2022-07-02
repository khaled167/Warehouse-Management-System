package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.Item;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {

	
	public List<Examination> findByExaminationCommittee(ExaminationCommittee ec);
	
	List<Examination> findByStockItemAndStockEntryDateAndStockExpiredDateAndStockPriceAndStockStatus(
			Item i, Date end,Date exp,double price,String status);
}
