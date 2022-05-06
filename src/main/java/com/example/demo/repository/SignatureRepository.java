package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Signature;

public interface SignatureRepository extends JpaRepository<Signature,Long> {

}
