package com.example.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Action;
import com.example.demo.entity.Holder;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class DeanService {
	
	
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository uRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private WarehouseRepository whRep;
	@Autowired private StockRepository stRep;
	public List<Action> getWarehouseActions(long whid) {
		
		return actRep.getWarehouseAction(whid);
		
	}
	public String makeTransaction(Holder input, long uid) {
		System.out.println("DEBUG1: "+input);
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("تحويل طلب من عميد","",date);
		actRep.save(act);
		Signature sign = new Signature(act,uRep.findById(uid).get(),date,date);
		signRep.save(sign);
		List<Transaction> inp = input.getP().getList();
		long to = input.getWarehouse().getWarehouse_id();
		for(Transaction t : inp) 
			t.setAction(act);
		
		transRep.saveAll(inp);
		
		for(Transaction t : inp) {
			Stock st = t.getStock();
			Stock depSt = stRep.getStock(st.getItem().getItem_id(),st.getEntry_date(),st.getExpired_date(), st.getPrice(), st.getStatus(),to);
			if( depSt != null) 
				depSt.setQuantity(depSt.getQuantity() + Math.min(t.getStock().getQuantity()   ,  t.getQuantity()) );
			
			else {
				depSt = new Stock();
				depSt.clone(st);
				depSt.setWarehouse(whRep.findById(to).get());
				depSt.setQuantity(Math.min(t.getStock().getQuantity()  ,  t.getQuantity()));
				stRep.save(depSt);
				}
			t.setQuantity(Math.min(st.getQuantity(), t.getQuantity()));
			st.setQuantity(Math.max(0, st.getQuantity()-t.getQuantity()));
			transRep.save(t);
			stRep.save(st);
		}
		
		return "DONE";

	
	}
	
	
	
}
