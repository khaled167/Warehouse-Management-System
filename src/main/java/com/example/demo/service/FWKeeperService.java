package com.example.demo.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Action;
import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationMember;
import com.example.demo.entity.ExaminationMemberSignature;
import com.example.demo.entity.ExaminationParam;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ExaminationComitteeRepository;
import com.example.demo.repository.ExaminationMemberRepository;
import com.example.demo.repository.ExaminationMemberSignatureRepository;
import com.example.demo.repository.ExaminationRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class FWKeeperService {
	
	@Autowired private TransactionRepository transRep;
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository userRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private RefundRepository refRep;
	@Autowired private ExaminationComitteeRepository ecRep;
	@Autowired private ExaminationRepository eRep;
	@Autowired private ExaminationMemberRepository emRep;
	@Autowired private ExaminationMemberSignatureRepository emsRep;
	@Autowired private WarehouseRepository whRep;
	@Autowired private StockRepository stRep;
	
	public String makeTransaction(Pair<Transaction> input,long uid) {
		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب تحويل",input.notes,date);
		// SEEN DATE MUST BE ADJUSTED
		Signature sign = new Signature(act,userRep.findById(uid).get(),date,date);
		signRep.save(sign);
		System.out.println("Trans: "+input.list);
		for(Transaction trans : input.list)
			trans.setAction(act);
		actRep.save(act);
		for(Transaction t : input.getList()) {
		long depWH = whRep.getWarehouse(t.getRequest().getAction().getAction_id());
		System.out.println("depWH: "+depWH);
		Stock st = t.getStock();
		Stock depSt = stRep.getStock(st.getItem().getItem_id(),st.getEntry_date(),st.getExpired_date(), st.getPrice(), st.getStatus(),depWH);
		if( depSt != null) {
//			if(depSt.getWarehouse().getWarehouse_id() != st.getWarehouse().getWarehouse_id()) {
			System.out.println("depSt is NOT NULL");
			System.out.println("Dep Stock: "+depSt);
			System.out.println("St: "+st);
			System.out.println("Before Dep Stock Quant: "+depSt.getQuantity());
			depSt.setQuantity(depSt.getQuantity() + Math.min(t.getStock().getQuantity()   ,  t.getQuantity()) );
			System.out.println("After Dep Stock Quant: "+depSt.getQuantity());
//			}
		}
		
		
		else {
			System.out.println("depSt is NULL");
			depSt = new Stock();
			depSt.clone(st);
			depSt.setWarehouse(whRep.findById(depWH).get());
			depSt.setQuantity(Math.min(t.getStock().getQuantity()  ,  t.getQuantity()));
			System.out.println("Dep Quant: "+depSt.getQuantity());
			}
		t.setQuantity(Math.min(st.getQuantity(), t.getQuantity()));
		st.setQuantity(Math.max(0, st.getQuantity()-t.getQuantity()));
		System.out.println("-----------------------------------------------------------");
		System.out.println("St: "+st);
		System.out.println("DepSt: "+depSt);
		transRep.save(t);
		stRep.saveAll(List.of(st,depSt));
		}
	
		return "DONE";
	}
	public List<Action> getActions(String type){
		return actRep.getAllActions(type);
	}

	public String makeExamine(ExaminationParam param, long uid) {
		// TO BE HARD TESTED IN FUTURE
		Date date = new Date(System.currentTimeMillis());
		// TO BE TESTED WITH FRON-END
		Action act = new Action(param.getEx_type(),param.getNotes(),date);
		actRep.save(act);
		param.getEc().setAction(act);
		ecRep.save(param.getEc());
		
		
		for(Examination ex : param.getList())
			ex.setExaminationComittee(param.getEc());
		
		// TO BE TESTED WITH FRONT END
		if(param.getEx_type().equals("فحص اصناف جديدة")) {
			param.getEc().setExamination_type(param.getEx_type());
		for(Examination ex : param.getList()) {
			Stock st = ex.getStock();
			st.setEntry_date(date);
			st.setWarehouse(whRep.findById(whRep.getWarehouseIdByUserId(uid)).get());
			if(ex.isIs_accepted()) {
				// TEMPORARY FACULTY WAREHOUSE = 1 (WHEN LOGGING IN)
				Stock facSt = stRep.getStock(st.getItem().getItem_id(),st.getEntry_date(),st.getExpired_date(), st.getPrice(), st.getStatus(),1);
				if(facSt != null) {
					facSt.setQuantity(facSt.getQuantity() + st.getQuantity());
					st.clone(facSt);
				}
			}
			else 
				st.setStatus("مرفوض");
			stRep.save(st);
		}
		}
		else {
			long aid = param.getEc().getAction().getAction_id();
			long depWH = whRep.getWarehouse(aid);
			List<Refund> ref =  refRep.getAllRefundDetails(aid);
			for(Refund r : ref) {
				Stock st = r.getTransaction().getStock();
				Stock depSt = stRep.getStock(st.getItem().getItem_id(),st.getEntry_date(),st.getExpired_date(), st.getPrice(), st.getStatus(),depWH);
				st.setQuantity(st.getQuantity() + r.getRefund_quantity());
				depSt.setQuantity(depSt.getQuantity() - r.getRefund_quantity());
				stRep.saveAll(List.of(st,depSt));
			}
			
		}
		eRep.saveAll(param.getList());
		emRep.saveAll(param.getExmem());
		
		for(ExaminationMember exm : param.getExmem()) 
			emsRep.save(new ExaminationMemberSignature(param.getEc(),exm,date));
		
		// SEEN DATE MUST BE ADJUSTED
		signRep.save(new Signature(act,userRep.findById(uid).get(),date,date));
		return "DONE";
	}

}		
