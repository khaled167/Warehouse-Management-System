package com.example.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Action;
import com.example.demo.entity.ActionHolder;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.query.Criteria;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestHolder;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.RoleRepository;
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
	@Autowired private RoleRepository roleRep;
	@Autowired private Criteria criteria;
	
	
	public List<ActionHolder> findTransactionAction(long depid){
		List<ActionHolder> res = new ArrayList<>();
		List<Action> act = criteria.findTransactionAction(depid);
		for(Action a : act) {
			long aid = a.getAction_id();
			String an = a.getActionNotes();
			Date ad = a.getActionDate();
			String wn =(signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(aid).get()).getRole().getWarehouse().getWarehouseName());
			String lu = userRep.findById( criteria.getLastUser(aid)).get().getFullname();
			int ap = 0;
			res.add(new ActionHolder(aid,an,ad,ap,wn,lu));
		}
		return res;
	}
	
	public List<ActionHolder> findAction(String type,long depid){
		List<ActionHolder> res = new ArrayList<>();
		System.out.println(type);
		long warehouseId  = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(depid).get()).getWarehouse().getWarehouse_id();
		List<Action> act = getActionType(type.equals("requests") ? "طلب اضافة" : "طلب استرجاع",warehouseId);
		for(Action a : act) {
			long aid = a.getAction_id();
			String an = a.getActionNotes();
			Date ad = a.getActionDate();
			String wn =(signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(aid).get()).getRole().getWarehouse().getWarehouseName());
			String lu = userRep.findById( criteria.getLastUser(aid)).get().getFullname();
			int ap = 0;
			if(type.equals("requests")) {
			List<Request> reqs = reqRep.findByAction(a);
			for(Request req : reqs) 
				ap += req.getCompletenessPercentage();
			ap = (ap / reqs.size());
			}
			res.add(new ActionHolder(aid,an,ad,ap,wn,lu));
		}
		return res;
	}
	
	public List<Action> getActionType(String actionType,long depid){
		return criteria.getActionType(actionType, roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(depid).get()).getWarehouse().getWarehouse_id());
//		return actRep.getActionType(actionType,warehouseId);
	}
	// DEFINE THE GENERAL WAREHOUSE TAKEN FROM
	public String makeRequestAction(Pair<RequestHolder> input,long uid) {
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب اضافة",input.notes,date);
		actRep.save(act);
		List<Request> list = new ArrayList<>();
		for(int i = 0;i<input.getList().size();i++) {
			Request r = new Request(act,itRep.getById(input.getList().get(i).getItemId()),input.getList().get(i).getExchange_reason(),input.getList().get(i).getNotes(),input.getList().get(i).getRequested_quantity());
			list.add(r);
		}

		// SEEN DATE MUST BE ADJUSTED
		Signature signature = new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()),date,date);
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
		Signature signature = new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()),date,date);
		signRep.save(signature);
		refRep.saveAll(list);
		return "DONE";
	}
	
	public List<Stock> getAllStocks(long uid){
		long warehouseId = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id();
		return stockRep.findByWarehouse(whRep.findById(warehouseId).get());
	}

	public List<Request> getRequestDetails(long aid) {
		return reqRep.findByAction(actRep.findById(aid).get());
	}

	public List<Refund> getRefundDetails(long aid) {
		return refRep.findByAction(actRep.findById(aid).get());
	}
	
	public List<Transaction> getTransactionDetails(long aid,long depid){
		return transRep.findByActionAndWarehouse(actRep.findById(aid).get(), roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(depid).get()).getWarehouse());
	}

	
}
