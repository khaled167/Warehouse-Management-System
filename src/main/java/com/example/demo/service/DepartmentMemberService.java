package com.example.demo.service;

import java.sql.Date;
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
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

@Service
public class DepartmentMemberService {

	
	
	@Autowired private ActionRepository actRep;
	@Autowired private RequestRepository reqRep;
	@Autowired private RefundRepository refRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private UserRepository userRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private StockRepository stockRep;
	public List<Action> getActionType(String actionType,long warehouseId){
		return actRep.getActionType(actionType,warehouseId);
	}
	
	public String makeRequestAction( Pair<Request> input,long uid) {
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب اضافة",input.notes,date);
		actRep.save(act);
		for(Request req : input.list)
			req.setAction(act);
		// SEEN DATE MUST BE ADJUSTED
		Signature signature = new Signature(act,userRep.findById(uid).get(),date,date);
		signRep.save(signature);
		reqRep.saveAll(input.list);
		return "DONE";
	}
	
	public String makeRefundAction(Pair<Refund> input,long uid) {
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب استرجاع",input.notes,date);
		actRep.save(act);
		for(Refund ref : input.list)
			ref.setAction(act);
		// SEEN DATE MUST BE ADJUSTED
		Signature signature = new Signature(act,userRep.findById(uid).get(),date,date);
		signRep.save(signature);
		refRep.saveAll(input.list);
		return "DONE";
	}
	
	public List<Stock> getAllStocks(long warehouseId){
		return stockRep.getAllStocks(warehouseId);
	}

	public List<Request> getRequestDetails(long aid) {
		return reqRep.getAllRequestDetails(aid);
	}

	public List<Refund> getRefundDetails(long aid) {
		return refRep.getAllRefundDetails(aid);
	}
	
	public List<Transaction> getTransactionDetails(long aid) {
		return transRep.getAllTransactionDetails(aid);
	}
	
	
}
