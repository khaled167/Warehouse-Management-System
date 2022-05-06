package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {


	@Query(value = " SELECT * FROM warehouses w WHERE w.is_available = 1  && w.warehouse_name != 'لايوجد'", nativeQuery = true)
	List<Warehouse> findAllActiveWarehouse();

	@Query(value = " SELECT count(*) FROM warehouses w WHERE w.is_available = 1  && w.warehouse_name != 'لايوجد'", nativeQuery = true)
	long countAllActiveWarehouse();
	
	@Query(value = " SELECT * FROM warehouses w WHERE w.is_available = 0", nativeQuery = true)
	List<Warehouse> findAllDeletedWarehouse();

	@Query(value = "SELECT warehouse_id from roles where user_id = ?1 order by date_of_assign DESC limit 1", nativeQuery = true)
	long getWarehouseIdByUserId(long userId);

	@Query(value = "(SELECT warehouse_id from roles where user_id ="
			+ " (SELECT user_id FROM signatures WHERE action_id = ?1 order by submit_date ASC limit 1)"
			+ "order by date_of_assign DESC limit 1 )", nativeQuery = true)
	long getWarehouse(long actionId);

}
