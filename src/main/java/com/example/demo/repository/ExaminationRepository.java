package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Examination;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {

}
