package com.example.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Action;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestHolder;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.Tuple;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class DepartmentMemberService {

	
	
	@Autowired private ActionRepository actRep;
	@Autowired private RequestRepository reqRep;
	@Autowired private RefundRepository refRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private UserRepository userRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private StockRepository stockRep;
	@Autowired private WarehouseRepository whRep;
	@Autowired private ItemRepository itRep;

	public List<Action> getActionType(String actionType,long warehouseId){
		return actRep.getActionType(actionType,warehouseId);
	}
	
	public String makeRequestAction(Pair<RequestHolder> input,long uid) {
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب اضافة",input.notes,date);
		actRep.save(act);
		List<Request> list = new ArrayList<>();
		for(int i = 0;i<input.getList().size();i++) {
			Request r = new Request(act,itRep.getById(input.getList().get(i).getItemId()),input.getList().get(i).getExchange_reason(),input.getList().get(i).getNotes(),0,input.getList().get(i).getRequested_quantity());
			list.add(r);
		}

		// SEEN DATE MUST BE ADJUSTED
		Signature signature = new Signature(act,userRep.findById(uid).get(),date,date);
		signRep.save(signature);
		reqRep.saveAll(list);
		return "DONE";
	}
	
	public String makeRefundAction(Pair<Tuple> input,long uid) {
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب استرجاع",input.notes,date);
		actRep.save(act);
		
		List<Refund> list = new ArrayList<>();
		for(int i = 0;i<input.list.size();i++) {
			Refund r = new Refund(act,transRep.getById((Long)input.list.get(i).getId()),(double)input.list.get(i).getValue());
			list.add(r);
		}
		
		for(Refund ref : list)
			ref.setAction(act);
		// SEEN DATE MUST BE ADJUSTED
		Signature signature = new Signature(act,userRep.findById(uid).get(),date,date);
		signRep.save(signature);
		refRep.saveAll(list);
		return "DONE";
	}
	
	public List<Stock> getAllStocks(long warehouseId){
		return stockRep.findByWarehouse(whRep.findById(warehouseId).get());
	}

	public List<Request> getRequestDetails(long aid) {
		return reqRep.findByAction(actRep.findById(aid).get());
	}

	public List<Refund> getRefundDetails(long aid) {
		return refRep.findByAction(actRep.findById(aid).get());
	}
	
	public List<Transaction> getTransactionDetails(long aid) {
		return transRep.findByAction(actRep.findById(aid).get());
	}
	
	
}
