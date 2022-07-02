package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.ExaminationMemberSignature;

public interface ExaminationMemberSignatureRepository extends JpaRepository<ExaminationMemberSignature, Long> {

	public List<ExaminationMemberSignature> findByExaminationCommittee(ExaminationCommittee ec);
	
}
