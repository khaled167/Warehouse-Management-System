package com.example.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Action;
import com.example.demo.entity.ActionHolder;
import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.ExaminationMember;
import com.example.demo.entity.ExaminationMemberSignature;
import com.example.demo.entity.ExaminationParam;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.RefundExamination;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Warehouse;
import com.example.demo.query.Criteria;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ExaminationCommitteeRepository;
import com.example.demo.repository.ExaminationMemberRepository;
import com.example.demo.repository.ExaminationMemberSignatureRepository;
import com.example.demo.repository.ExaminationRepository;
import com.example.demo.repository.Quadra;
import com.example.demo.repository.RefundExaminationRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.Triple;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

@Service
public class FWKeeperService {
	
	@Autowired private TransactionRepository transRep;
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository userRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private RefundRepository refRep;
	@Autowired private ExaminationCommitteeRepository ecRep;
	@Autowired private ExaminationRepository eRep;
	@Autowired private ExaminationMemberRepository emRep;
	@Autowired private ExaminationMemberSignatureRepository emsRep;
	@Autowired private WarehouseRepository whRep;
	@Autowired private StockRepository stRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private RequestRepository reqRep;
	@Autowired private RefundExaminationRepository reRep;
	@Autowired private Criteria criteria;
	
	

	public List<Refund> findRefundDetails(long fwkid,long actid){
		Warehouse warehouse = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwkid).get()).getWarehouse();
		String warehouseName = warehouse.getWarehouseName();
		return refRep.findByActionAndTransactionRequestItemCategory(actRep.findById(actid).get(), warehouseName);
	}
	
	public List<Request> findRequestDetails(long fwkid,long actid){
		Warehouse warehouse = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwkid).get()).getWarehouse();
		String warehouseName = warehouse.getWarehouseName();
		return reqRep.findByActionAndItemCategory(actRep.findById(actid).get(), warehouseName);
	}
	
	
	public List<ActionHolder> findRefundAction(long fwkid){
		List<ActionHolder> res = new ArrayList<>();
		List<Action> act = criteria.findRefundAction(fwkid);
		for(Action a : act) {
			long aid = a.getAction_id();
			String an = a.getActionNotes();
			Date ad = a.getActionDate();
			String wn =(signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(aid).get()).getRole().getWarehouse().getWarehouseName());
			String lu = userRep.findById(criteria.getLastUser(aid)).get().getFullname();
			int ap = 0;			
			res.add(new ActionHolder(aid,an,ad,ap,wn,lu));
		}
		return res;
	}
	// GENERAL WAREHOUSE
	public List<ActionHolder> findRequestAction(long fwkid){
		List<ActionHolder> res = new ArrayList<>();
		List<Action> act = criteria.findRequestAction(fwkid);
		for(Action a : act) {
			long aid = a.getAction_id();
			String an = a.getActionNotes();
			Date ad = a.getActionDate();
			String wn =(signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(aid).get()).getRole().getWarehouse().getWarehouseName());
			String lu = userRep.findById(criteria.getLastUser(aid)).get().getFullname();
			int ap = 0;
			List<Request> reqs = reqRep.findByAction(a);
			int size = 0;
			for(Request req : reqs)
				if(req.getItem().getCategory().equals(roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwkid).get()).getWarehouse().getWarehouseName())) {
					ap += req.getCompletenessPercentage();
					size++;
				}
				ap = (ap / size);
			
			res.add(new ActionHolder(aid,an,ad,ap,wn,lu));
		}
		return res;
	}
	
	
	public List<ActionHolder> findTransactionAction(long uid){
		List<ActionHolder> res = new ArrayList<>();
		long warehouseId = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id();
		List<Action> act = criteria.getActionType("طلب تحويل",warehouseId);
		for(Action a : act) {
			long aid = a.getAction_id();
			String an = a.getActionNotes();
			Date ad = a.getActionDate();
			String wn =(signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(aid).get()).getRole().getWarehouse().getWarehouseName());
			String lu = userRep.findById(criteria.getUser(aid)).get().getFullname();
			int ap = 0;
			res.add(new ActionHolder(aid,an,ad,ap,wn,lu));
		}
		return res;
	}
		
	public String makeTransaction(Pair<Triple> input,long uid) {
		List<Transaction> list = new ArrayList<>();

		Date date = new Date(System.currentTimeMillis());
		Action act = new Action("طلب تحويل",input.notes,date);
		actRep.save(act);
		// SEEN DATE MUST BE ADJUSTED
		System.out.println(roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get())+" here");
		Signature sign = new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()),date,date);
		signRep.save(sign);
		for(int i = 0;i<input.list.size();i++) {
			Transaction t = new Transaction(act , reqRep.getById(input.list.get(i).getRid()),stRep.getById(input.list.get(i).getSid()),input.list.get(i).getQuantity());
			list.add(t);
		}
	
		for(Transaction t : list) {
		long depWH = signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(t.getRequest().getAction().getAction_id()).get()).getRole().getWarehouse().getWarehouse_id();
		t.setWarehouse(whRep.findById(depWH).get());
		System.out.println("depWH: "+depWH);
		Stock st = t.getStock();
		Stock depSt = stRep.findByItemAndEntryDateAndExpiredDateAndPriceAndStatusAndWarehouse(st.getItem(),st.getEntryDate(),st.getExpiredDate(), st.getPrice(), st.getStatus(),whRep.findById(depWH).get());
		if( depSt != null) 
			depSt.setQuantity(depSt.getQuantity() + Math.min(t.getStock().getQuantity(), t.getQuantity()) );
		
		else {
			depSt = new Stock();
			depSt.clone(st);
			depSt.setWarehouse(whRep.findById(depWH).get());
			depSt.setQuantity(Math.min(t.getStock().getQuantity(), t.getQuantity()));
			}
		t.setQuantity(Math.min(st.getQuantity(), t.getQuantity()));
		st.setQuantity(Math.max(0, st.getQuantity()-t.getQuantity()));
		stRep.saveAll(Arrays.asList(st,depSt));
		Request req = t.getRequest();
		req.setReceived_quantity(req.getReceived_quantity()+t.getQuantity());
		transRep.save(t);
		}
		return "DONE";
	}
	public List<Action> getActions(String type){
		return actRep.findByActionTypeOrderByActionDateDesc(type);
	}

	@Transactional
	public String makeItemsExamination(ExaminationParam <Examination> param,long uid) {
		// TO BE HARD TESTED IN FUTURE
		Date date = new Date(System.currentTimeMillis());
		// TO BE TESTED WITH FRON-END
		Action act = new Action("فحص اصناف جديدة",param.getNotes(),date);
		actRep.save(act);
		param.getEc().setAction(act);
		ecRep.save(param.getEc());
			List<Examination> list = new ArrayList<>();
			for(int i = 0 ;i<param.getList().size();i++) {
				Examination ex = new Examination(param.getEc(),param.getList().get(i).getStock(),param.getList().get(i).getPercentage_examined(),param.getList().get(i).isAccepted(),param.getList().get(i).getNotes());
				list.add(ex);
			}
			for(Examination ex : list) {
			Stock st = ex.getStock();
			ex.setQuantityExamined(ex.getStock().getQuantity());
			st.setEntryDate(date);
			st.setWarehouse(roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse());
			if(ex.isAccepted()) {
				// TEMPORARY FACULTY WAREHOUSE = 1 (WHEN LOGGING IN)
				Stock facSt = stRep.findByItemAndEntryDateAndExpiredDateAndPriceAndStatusAndWarehouse(st.getItem(),st.getEntryDate(),st.getExpiredDate(), st.getPrice(), st.getStatus(),whRep.findById(1l).get());
				if(facSt != null) {
					facSt.setQuantity(facSt.getQuantity() + st.getQuantity());
					st.clone(facSt);
				}
			}
			else 
				st.setStatus("مرفوض");
			stRep.save(st);

		}
		eRep.saveAll(list);
		emRep.saveAll(param.getExmem());
		
		for(ExaminationMember exm : param.getExmem()) 
			emsRep.save(new ExaminationMemberSignature(param.getEc(),exm,date));
		
		// SEEN DATE MUST BE ADJUSTED
		signRep.save(new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()),date,date));
		return "DONE";

	}
	public String makeRefundsExamination(ExaminationParam<Quadra> param, long uid) {
		// TO BE HARD TESTED IN FUTURE
		Date date = new Date(System.currentTimeMillis());
		// TO BE TESTED WITH FRON-END
		Action act = new Action("فحص اصناف مرتجعة",param.getNotes(),date);
		actRep.save(act);
		param.getEc().setAction(act);
		ecRep.save(param.getEc());
		
		// TO BE TESTED WITH FRONT END
	
			List<RefundExamination> list = new ArrayList<>();
			for(int i = 0 ;i<param.getList().size();i++) {
				RefundExamination ex = new RefundExamination(param.getEc(),refRep.getById(param.getList().get(i).getId()),param.getList().get(i).getPercentage(),param.getList().get(i).isAccepted(),param.getList().get(i).getNotes());
				list.add(ex);
			}
			for(RefundExamination re : list) {
				if(re.isAccepted()) {
					Refund r = re.getRefund();
					long depWH = signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(r.getAction().getAction_id()).get()).getRole().getWarehouse().getWarehouse_id();
					Stock st = r.getTransaction().getStock();
					Stock depSt = stRep.findByItemAndEntryDateAndExpiredDateAndPriceAndStatusAndWarehouse(st.getItem(),st.getEntryDate(),st.getExpiredDate(), st.getPrice(), st.getStatus(),whRep.findById(depWH).get());
					st.setQuantity(st.getQuantity() + r.getRefund_quantity());
					depSt.setQuantity(depSt.getQuantity() - r.getRefund_quantity());
					stRep.saveAll(Arrays.asList(st,depSt));
				}
			}
		reRep.saveAll(list);
		emRep.saveAll(param.getExmem());
		
		for(ExaminationMember exm : param.getExmem()) 
			emsRep.save(new ExaminationMemberSignature(param.getEc(),exm,date));
		
		// SEEN DATE MUST BE ADJUSTED
		signRep.save(new Signature(act,roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()),date,date));
		return "DONE";
	}
	public List<Stock> getAllStocks(long uid) {
		long warehouseId = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id();
	 	return stRep.findByWarehouse(whRep.findById(warehouseId).get());
	}
	
	public List<ExaminationCommittee> getExaminations() {
		return criteria.getExaminations();
	}
	public List<Examination> getExaminationDetails(long examinationId) {
	
		return criteria.getExaminationDetails(examinationId);
	}
	public List<RefundExamination> getRefundExaminationDetails(long refexaminationId) {
		
		return criteria.getRefundExaminationDetails(refexaminationId);
	}
	public List<ExaminationMemberSignature> getExaminationMembers(long examinationId) {
		return criteria.getExaminationMembers(examinationId);
	}
	
	public Object isExamined(long fwkid,long aid) {
		List<Refund> ref = findRefundDetails(fwkid,aid);
		System.out.println("ref: "+ref);
		List<RefundExamination> refexList = reRep.findByRefundIn(ref);
		System.out.println("refexList: "+refexList);	
		boolean found = ! (refexList == null | refexList.isEmpty());
		return new Object[] { found , found ? refexList.get(0).getExaminationCommittee().getExamination_committee_id() : 0};
	}
}		
