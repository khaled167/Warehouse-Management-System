package com.example.demo.controller;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Action;
import com.example.demo.entity.ActionHolder;
import com.example.demo.entity.AdminAction;
import com.example.demo.entity.AdminEditDetails;
import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.ExaminationMemberSignature;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Warehouse;
import com.example.demo.query.Criteria;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.Triple;
import com.example.demo.repository.Tuple;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.DeanService;
import com.example.demo.service.DepartmentMemberService;


@RestController
@RequestMapping("/dean/{deanid}")
@CrossOrigin("http://sciwarehouse.herokuapp.com")
public class DeanController {
	@Autowired private RefundRepository refRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private RequestRepository reqRep;
	@Autowired private DeanService ds;
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository userRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private DeanService deanServ;
	@Autowired private WarehouseRepository whRep;
	@Autowired private DepartmentMemberService dms;
	@Autowired private StockRepository stRep;
	@Autowired private Criteria criteria;

		
	@GetMapping("instocks/{stid}")
	public List<Transaction> inStock(@PathVariable("stid") long stid,@PathVariable("deanid") long deanid){
		return criteria.inStocks(stid, deanid);
	}
	@GetMapping("outstocks/{stid}")
	public List<Transaction> outStock(@PathVariable("stid") long stid){
		return criteria.outStocks(stid);
	}
	@GetMapping("/{act_type}/actiondetails")
	public List<ActionHolder> findAction(@PathVariable("act_type")String type,@PathVariable("deanid")long deanid) {
		return deanServ.findAction(type);	
	}
	
	
	@GetMapping("/requests/{act_id}")
	public List<Request> getAllRequest(@PathVariable("act_id") long aid){
		return reqRep.findByAction(actRep.findById(aid).get());
	}
	
	@GetMapping("/refunds/{act_id}")
	public List<Refund> getAllRefund(@PathVariable("act_id") long aid){
		return refRep.findByAction(actRep.findById(aid).get());
	}
	
	@GetMapping("/transactions/{act_id}")
	public List<Transaction> getAllTransaction(@PathVariable("act_id") long aid){
		return transRep.findByAction(actRep.findById(aid).get());
	}
	
//	@PostMapping("/maketransactions")
//	public String makeTransaction(@RequestBody Holder<Tuple> input,@PathVariable("deanid") long uid) {
////		return fwkServ.makeTransaction(input, uid);
//		return deanServ.makeTransaction(input,uid);
//	}
	
	
	@PostMapping("/maketransaction1")
	public String makeTransaction1(@RequestBody Pair<Triple> input,@PathVariable("deanid") long uid) {
//		return fwkServ.makeTransaction(input, uid);
		return deanServ.makeTransaction1(input,uid);
	}
	
	@RequestMapping("/{warehouseid}/actions")
	public List<Action> getWarehouseActions(@PathVariable("warehouseid") long whid){
		return ds.getWarehouseActions(whid);
	}
	
	@RequestMapping("/{warehouseid}/actions/{actiontype}")
	public List<Action> getSpecificAction(@PathVariable("warehouseid") long whid,@PathVariable("actiontype")String type){
		return dms.getActionType(type, whid);
	}
	
	@PostMapping("/requests")
	public String setAllowedQuantity(@RequestBody List<Tuple> inp,@PathVariable("deanid") long did) {
		List<Request> list = new ArrayList<>();
		for(int i = 0;i<inp.size();i++) {
			Request r = reqRep.getById((Long)inp.get(i).getId());
			r.setAllowed_quantity((double)inp.get(i).getValue());
			list.add(r);
		}
		long aid = list.get(0).getAction().getAction_id();
		Date date = new Date(System.currentTimeMillis());
		Signature sign = new Signature(actRep.findById(aid).get(),roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(did).get()),date,date);
		signRep.save(sign);
		reqRep.saveAll(list);
		return "DONE";
	}
	
	@GetMapping("/warehouse/{act_id}")
	public  Warehouse getWarehouse(@PathVariable("act_id")long actid) {
		return signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(actid).get()).getRole().getWarehouse();
	}
	@GetMapping("signatures/{act_id}")
	List<Signature> getSignatures(@PathVariable("act_id") long aid){
		return signRep.findByAction(actRep.findById(aid).get());
	}

	@GetMapping("/examinations")
	public  List<ExaminationCommittee> getExaminations() {
		return deanServ.getExaminations();
	}
	
	@GetMapping("/examinations/{examination_id}")
	public  List<Examination> getExaminationDetails(@PathVariable("examination_id")long examinationId) {
		return deanServ.getExaminationDetails(examinationId);
	}
	@GetMapping("/examinations/{examination_id}/members")
	public  List<ExaminationMemberSignature> getExaminationMembers(@PathVariable("examination_id")long examinationId) {
		return deanServ.getExaminationMembers(examinationId);
	}
	@GetMapping("/adminactions")
	public  List<AdminAction> getAdminActions() {
		return deanServ.getAdminActions();
	}
	@GetMapping("/adminactions/{edit_id}")
	public  List<AdminEditDetails> getAdminEditHistory(@PathVariable("edit_id")long adminEditActionId) {
		return deanServ.getAdminEditHistory(adminEditActionId);
	}
	
	@GetMapping("/warehouses")
	public List<Warehouse> getAllWarehouses(@PathVariable("deanid") long deanid) {
		return whRep.findByIsAvailableAndWarehouseNameNot(true,"لايوجد");
	}
	
	@GetMapping("warehouses/{whid}")
	public List<Stock> getAllWarehouseStocks(@PathVariable("deanid") long deanid,@PathVariable("whid")long whid){
		return stRep.findByWarehouse(whRep.findById(whid).get());
	}
	
	@GetMapping("stock/{stid}")
	public Stock getStock(@PathVariable("stid") long stid, @PathVariable("deanid") long deanid) {
		return stRep.findById(stid).get();
	}
	
	
	@GetMapping("stockexams/{stid}")
	public List<Examination> getStockExaminations(@PathVariable("stid") long stid) {
		return criteria.getStockExaminations(stid);
	}
	@GetMapping("/transactionhistory/{reqid}")
	public List<Transaction> findTransactionByRequest(@PathVariable("reqid")long reqid){
		return criteria.findTransactionsByRequest(reqid);
	}
	
}
