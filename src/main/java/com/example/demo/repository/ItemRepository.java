package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	@Query(value=" SELECT * FROM items i WHERE i.is_available = 1", nativeQuery = true)
	List<Item> findAllActiveItems();
	
	@Query(value=" SELECT count(*) FROM items i WHERE i.is_available = 1", nativeQuery = true)
	long countAllActiveItems();

	
	@Query(value=" SELECT * FROM items i WHERE i.is_available = 0", nativeQuery = true)
	List<Item> findAllDeletedItems();

	
}
