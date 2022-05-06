package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ExaminationMember;

public interface ExaminationMemberRepository extends JpaRepository<ExaminationMember, Long>{

}
