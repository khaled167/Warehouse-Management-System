package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Item;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Warehouse;

public interface StockRepository extends JpaRepository<Stock,Long>{
	
	
	List<Stock> findByWarehouse(Warehouse warehouse);
		
	List<Stock> findByItemAndWarehouseAndStatus(Item item, Warehouse warehouse,String status);
			
	Stock findByItemAndEntryDateAndExpiredDateAndPriceAndStatusAndWarehouse
					(Item item,Date entryDate,Date expiredDate,double price,String status,Warehouse warehouse);
	
}
