package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Stock;

public interface StockRepository extends JpaRepository<Stock,Long>{
	@Query(value = "SELECT * FROM stocks where warehouse_id = ?1",nativeQuery=  true)
	List<Stock> getAllStocks(long warehouseId);

	
		@Query(value = "SELECT * FROM stocks where warehouse_id = ?2 && stock_id = ?1",nativeQuery = true)
	List<Stock> getItemStocks(long itid,long whid);
	
	@Query(value = "SELECT * FROM stocks where item_id = ?1 &&"
			+ " entry_date = ?2 &&"
			+ " expired_date = ?3 &&"
			+ " price = ?4 &&"
			+ " status = ?5 &&"
			+ " warehouse_id = ?6",
			nativeQuery = true)
	Stock getStock(long itemid,Date entrydate,Date expdate,double price,String status,long whid);
	
	
//	@Query(value = "SELECT * FROM stocks where item_id = ?1 &&"
//			+ " price = ?2 &&"
//			+ " status = ?3 &&"
//			+ " warehouse_id = ?4",
//			nativeQuery = true)
//	List<Stock> getStock(long itemid,double price,String status,long whid);

}
