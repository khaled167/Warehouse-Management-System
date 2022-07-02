package com.example.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Action;
import com.example.demo.entity.ActionHolder;
import com.example.demo.entity.AdminAction;
import com.example.demo.entity.AdminEditDetails;
import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.ExaminationMemberSignature;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.query.Criteria;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.AdminActionRepository;
import com.example.demo.repository.AdminEditDetailsRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.Triple;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class DeanService {
	
	
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository uRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private WarehouseRepository whRep;
	@Autowired private StockRepository stRep;
	@Autowired private AdminActionRepository adminActionRep;
	@Autowired private AdminEditDetailsRepository adminEditDetailsRep;
	@Autowired private RequestRepository reqRep;
	@Autowired private UserRepository userRep;

	

	@Autowired private Criteria criteria;
	
	public List<ActionHolder> findAction(String type){
		List<ActionHolder> res = new ArrayList<>();
		System.out.println(type);
		List<Action> act = getActions(type.equals("requests") ? "طلب اضافة" : type.equals("refunds") ? "طلب استرجاع" : "طلب تحويل");
		for(Action a : act) {
			long aid = a.getAction_id();
			String an = a.getActionNotes();
			Date ad = a.getActionDate();
			String wn =(signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(aid).get()).getRole().getWarehouse().getWarehouseName());
			String lu = userRep.findById( type.equals("transactions") ? criteria.getUser(aid) : criteria.getLastUser(aid)).get().getFullname();
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

	public List<Action> getActions(String type){
		return actRep.findByActionTypeOrderByActionDateDesc(type);
	}
	public List<Action> getWarehouseActions(long whid) {
		return criteria.getWarehouseAction(whid);
//		return actRep.getWarehouseAction(whid);
		
	}
//	public String makeTransaction(Holder<Tuple> input, long uid) {
//		System.out.println("DEBUG1: "+input);
//		Date date = new Date(System.currentTimeMillis());
//		Action act = new Action("تحويل طلب من عميد",input.getP().getNotes(),date);
//		actRep.save(act);
//		Signature sign = new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(uRep.findById(uid).get()),date,date);
//		signRep.save(sign);
//		List<Transaction> inp = new ArrayList<>();
//		for(int i=0;i<input.getP().getList().size();i++) {
//			Transaction trans =new Transaction(act,null,stRep.getById(input.getP().getList().get(i).getId()),input.getP().getList().get(i).getValue());
//			inp.add(trans);
//		}
//		long to = input.getWarehouseId();
//		for(Transaction t : inp) 
//			t.setAction(act);
//		
//		transRep.saveAll(inp);
//		
//		for(Transaction t : inp) {
//			Stock st = t.getStock();
//			Stock depSt = stRep.findByItemAndEntryDateAndExpiredDateAndPriceAndStatusAndWarehouse(st.getItem(),st.getEntryDate(),st.getExpiredDate(), st.getPrice(), st.getStatus(),whRep.getById(input.getWarehouseId()));
//			if( depSt != null) 
//				depSt.setQuantity(depSt.getQuantity() + Math.min(t.getStock().getQuantity()   ,  t.getQuantity()) );
//			
//			else {
//				depSt = new Stock();
//				depSt.clone(st);
//				depSt.setWarehouse(whRep.findById(to).get());
//				depSt.setQuantity(Math.min(t.getStock().getQuantity()  ,  t.getQuantity()));
//				stRep.save(depSt);
//				}
//			t.setQuantity(Math.min(st.getQuantity(), t.getQuantity()));
//			st.setQuantity(Math.max(0, st.getQuantity()-t.getQuantity()));
//			transRep.save(t);
//			stRep.save(st);
//		}
//		
//		return "DONE";
//
//	
//	}
	
	public String makeTransaction1(Pair<Triple> input, long uid) {
		System.out.println("DEBUG1: "+input);
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("تحويل طلب من عميد",input.getNotes(),date);
		actRep.save(act);
		Signature sign = new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(uRep.findById(uid).get()),date,date);
		signRep.save(sign);
		List<Transaction> inp = new ArrayList<>();
		for(int i=0;i<input.getList().size();i++) {
			Transaction trans =new Transaction(act,null,stRep.getById(input.getList().get(i).getRid()),input.getList().get(i).getQuantity());
			inp.add(trans);
		}
//		long to = input.getWarehouseId();
		for(Transaction t : inp) 
			t.setAction(act);
		
		transRep.saveAll(inp);
		
		for(int i = 0;i<input.getList().size();i++) {
			Transaction t = inp.get(i);
			long to = input.getList().get(i).getSid();
			t.setWarehouse(whRep.findById(to).get());
			Stock st = t.getStock();
			Stock depSt = stRep.findByItemAndEntryDateAndExpiredDateAndPriceAndStatusAndWarehouse(st.getItem(),st.getEntryDate(),st.getExpiredDate(), st.getPrice(), st.getStatus(),whRep.getById(to));
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

	
	public List<ExaminationCommittee> getExaminations() {
		return criteria.getExaminations();
	}
	public List<Examination> getExaminationDetails(long examinationId) {
	
		return criteria.getExaminationDetails(examinationId);
	}
	public List<ExaminationMemberSignature> getExaminationMembers(long examinationId) {
		return criteria.getExaminationMembers(examinationId);
	}
	public List<AdminAction> getAdminActions() {
		return adminActionRep.findAll();
	}
	public List<AdminEditDetails> getAdminEditHistory(long adminEditActionId) {
		return adminEditDetailsRep.findByAdminAction(adminActionRep.findById(adminEditActionId).get());
	}
	
}
