package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.ExaminationMemberSignature;
import com.example.demo.entity.ExaminationParam;
import com.example.demo.entity.Item;
import com.example.demo.entity.ItemMonthlyRequest;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.RefundExamination;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.query.Criteria;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ExaminationCommitteeRepository;
import com.example.demo.repository.ItemMonthlyRequestRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.Quadra;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.Triple;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DepartmentMemberService;
import com.example.demo.service.FWKeeperService;

@RestController
@RequestMapping("fwk/{fwkid}")
@CrossOrigin("http://localhost:3000")

public class FWKeeperController {
	
	@Autowired private FWKeeperService fwkServ;
	@Autowired private DepartmentMemberService dms;
	@Autowired private TransactionRepository transRep;
	@Autowired private StockRepository stRep;
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository userRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private ItemRepository itRep;	
	@Autowired private SignatureRepository signRep;
	@Autowired private ExaminationCommitteeRepository examinationCommitteeRepo;
	@Autowired private ItemMonthlyRequestRepository imrRep;
	@Autowired private Criteria criteria;
	
	
	@GetMapping("/datapre")
	public void makeData() {
		List<Item> list = itRep.findAll();
		Random r = new Random();
		for(int year = 2010;year<=2022;year++) {
			for(int month = 1;month<=12;month++) {
				for(Item item:list) {
				int quantity = r.nextInt(0,500);
				ItemMonthlyRequest imr = new ItemMonthlyRequest();
				imr.setItem(item);
				imr.setMonthNumber(month);
				imr.setYearNumber(year);
				imr.setQuantity(quantity);
				imrRep.save(imr);
				}
			}
		}
	}
	
	
	@GetMapping("averageitem/{month}")
	public List<Object> getAverageItem(@PathVariable("month") int month){
		List<Item> items = itRep.findAll();
		List<Object> res = new ArrayList<>();
		for(Item item : items) {
			List<ItemMonthlyRequest> imr = imrRep.findByItemAndMonthNumber(item, month);
			double quantity = 0;
			for(ItemMonthlyRequest im : imr)
				quantity += im.getQuantity();
			if(quantity > 0)
			quantity/=imr.size();
			res.add(new Object[] {item.getItem_id(),item.getItemName(),quantity});
		}
		return res;
	}
	
	@GetMapping("instocks/{stid}")
	public List<Transaction> inStock(@PathVariable("stid") long stid,@PathVariable("fwkid") long fwkid){
		return criteria.inStocks(stid, fwkid);
	}
	@GetMapping("outstocks/{stid}")
	public List<Transaction> outStock(@PathVariable("stid") long stid){
		return criteria.outStocks(stid);
	}
	
	@GetMapping("test/{act_id}")
	public Signature test(@PathVariable("act_id") long actid) {
		return signRep.findTopByActionOrderBySubmitDateAsc(actRep.findById(actid).get());
	}
	@GetMapping("/{act_type}")
	public List<Action> getAction(@PathVariable("act_type") String type){
		if(type.equals("requests"))
			return fwkServ.getActions("طلب اضافة");
		if(type.equals("refunds"))
			return fwkServ.getActions("طلب استرجاع");
		if(type.equals("deprives"))
			return fwkServ.getActions("طلب جرد");
		if(type.equals("transactions"))
			return fwkServ.getActions("طلب تحويل");
		else return null;
	}
	
//	@GetMapping("/requestdetails")
//	public List<ActionHolder> findRequestAction(@PathVariable("fwkid") long fwkid) {
//		return fwkServ.findRequestAction(fwkid);		
//	}
	
	@GetMapping("/refunds/actiondetails")
	public List<Object> findRefundAction(@PathVariable("fwkid") long fwkid) {
//		if(type.equals("requests"))
//			return fwkServ.findRequestAction(fwkid);
			return fwkServ.findRefundAction(fwkid);
//		if(type.equals("transactions"))
//			return fwkServ.findTransactionAction(fwkid);
	}
	@GetMapping("/requests/actiondetails")
	public List<ActionHolder> findRequestAction(@PathVariable("fwkid") long fwkid) {
			return fwkServ.findRequestAction(fwkid);
	
	}
	
	
	@GetMapping("/transactions/actiondetails")
	public List<ActionHolder> findTransactionAction(@PathVariable("fwkid") long fwkid) {
			return fwkServ.findTransactionAction(fwkid);
	}

//	@GetMapping("/doers/{actid}")
//	public List<Long> doers(@PathVariable long actid){
//		return fwkServ.doers(actid);
//	}
	
	@GetMapping("/requests/{act_id}")
	public List<Request> getAllRequest(@PathVariable("act_id") long aid,@PathVariable("fwkid") long fwkid){
		return fwkServ.findRequestDetails(fwkid, aid);
	}
	
	@GetMapping("/refunds/{act_id}")
	public List<Refund> getAllRefund(@PathVariable("act_id") long aid,@PathVariable("fwkid") long fwkid){
		return fwkServ.findRefundDetails(fwkid,aid);
	}
	
	@GetMapping("/transactions/{act_id}")
	public List<Transaction> getAllTransaction(@PathVariable("act_id") long aid){
		return transRep.findByAction(actRep.findById(aid).get());
	}
	
	@PostMapping("/maketransactions")
	public String makeTransaction(@RequestBody Pair<Triple> input,@PathVariable("fwkid") long uid) {
		System.out.println(input);
		return fwkServ.makeTransaction(input, uid);
	}
	
	@PostMapping("/itemsexamine")
	public String makeItemsExamination(@RequestBody ExaminationParam<Examination> param,@PathVariable("fwkid") long uid) {
		return fwkServ.makeItemsExamination(param,uid);
	}
	
	@PostMapping("/refundsexamine")
	public String makeRefundsExamination(@RequestBody ExaminationParam<Quadra> param,@PathVariable("fwkid") long uid) {
		return fwkServ.makeRefundsExamination(param,uid);
	}
	
	@GetMapping("/getitemstock/{itid}")
	public List<Stock> getItemStocks(@PathVariable("itid")long itid,@PathVariable("fwkid")long fwid){
		return stRep.findByItemAndWarehouseAndStatus(itRep.findById(itid).get(), roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwid).get()).getWarehouse(),"مقبول");
	}
		
	@PostMapping("/getstocks")
	public List<Stock> getStocks(@PathVariable("fwkid")long fwid,@RequestBody List<Long> list){
		return criteria.getStocks(list,fwid);
	}
	
	@GetMapping("getAllStocks")
	public List<Stock> getAllStocks(@PathVariable("fwkid")long fwid){
		return fwkServ.getAllStocks(fwid);
	}
	
	@GetMapping("signatures/{act_id}")
	List<Signature> getSignatures(@PathVariable("act_id") long aid){
		return signRep.findByAction(actRep.findById(aid).get());
	}
	@GetMapping("/stocks")
	public List<Stock> getStocks(@PathVariable("fwkid") long uid) {
		return dms.getAllStocks(uid);
	}
	
//	@GetMapping("getrefundexams/{refid}")
//	public List<RefundExamination> getRefundExam(@PathVariable("refid") long refid,@PathVariable("fwkid") long fwkid) {
//		return refexRep.findByRefund(refRep.findById(refid).get());
//	}
	
	@GetMapping("refundexaminations/{refundexamination_id}")
	public List<RefundExamination> getRefundExaminationDetails(@PathVariable("refundexamination_id")long refexaminationId){
		return fwkServ.getRefundExaminationDetails(refexaminationId);
	}
	
	@GetMapping("stockexams/{stid}")
	public List<Examination> getStockExaminations(@PathVariable("stid") long stid) {
		return criteria.getStockExaminations(stid);
	}
	
	@GetMapping("/examinations")
	public  List<ExaminationCommittee> getExaminations(@PathVariable("fwkid") long fwkid) {
//		return fwkServ.getExaminations();
			List<Action> list = criteria.findWarehouseExaminationActions(roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwkid).get()).getWarehouse().getWarehouse_id(),
					"فحص اصناف جديدة"); 
			return examinationCommitteeRepo.findByActionIn(list);
	}
	
	@GetMapping("/refundexaminations")
	public  List<ExaminationCommittee> getRefundExaminations(@PathVariable("fwkid") long fwkid) {
//		return fwkServ.getExaminations();
			List<Action> list = criteria.findWarehouseExaminationActions(roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwkid).get()).getWarehouse().getWarehouse_id(),
					"فحص اصناف مرتجعة"); 
			return examinationCommitteeRepo.findByActionIn(list);
	}
	
	
	@GetMapping("/examinations/{examination_id}")
	public  List<Examination> getExaminationDetails(@PathVariable("examination_id")long examinationId) {
		return fwkServ.getExaminationDetails(examinationId);
	}
	@GetMapping("/examinations/{examination_id}/members")
	public  List<ExaminationMemberSignature> getExaminationMembers(@PathVariable("examination_id")long examinationId) {
		return fwkServ.getExaminationMembers(examinationId);
	}
	
	@GetMapping("isexamined/{aid}")
	public Object isExamined(@PathVariable("aid") long aid,@PathVariable("fwkid") long fwkid) {
		return fwkServ.isExamined(fwkid,aid);
	}
	
	@GetMapping("/transactionhistory/{reqid}")
	public List<Transaction> findTransactionByRequest(@PathVariable("reqid")long reqid){
		return criteria.findTransactionsByRequest(reqid);
	}
}
