package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {


	
	List<Warehouse> findByIsAvailableAndWarehouseNameNot(boolean availability,String whname);
	
//	@Query(value = "(SELECT warehouse_id from roles where user_id ="
//			+ " (SELECT user_id FROM signatures WHERE action_id = ?1 order by submit_date ASC limit 1)"
//			+ "order by date_of_assign DESC limit 1 )", nativeQuery = true)
//	long getWarehouse(long actionId);
	List<Warehouse> findByWarehouseType(String warehouseType);
	Warehouse findByWarehouseName(String warehouseName);

}
