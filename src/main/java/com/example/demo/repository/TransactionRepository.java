package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Action;
import com.example.demo.entity.Item;
import com.example.demo.entity.Request;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Warehouse;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
	
	
	List<Transaction> findByAction(Action action);
	List<Transaction> findByStock(Stock stock);
	List<Transaction> findByWarehouseAndStockItemAndStockEntryDateAndStockExpiredDateAndStockPriceAndStockStatus(
			Warehouse w,Item i, Date end,Date exp,double price,String status);
	List<Transaction> findByActionAndWarehouse(Action action,Warehouse warehouse);
	
	List<Transaction> findByRequest(Request req);
	
	List<Transaction> findByStockItem(Item item);
}
