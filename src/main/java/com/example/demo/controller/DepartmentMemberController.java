package com.example.demo.controller;

import java.util.ArrayList;
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
import com.example.demo.entity.Examination;
import com.example.demo.entity.Item;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.query.Criteria;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RequestHolder;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.Tuple;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DepartmentMemberService;

@RestController
@RequestMapping("dep_memb/{depid}")
@CrossOrigin("http://localhost:3000")

public class DepartmentMemberController {

	@Autowired private DepartmentMemberService dms;
	@Autowired private UserRepository userRep;
	@Autowired private ItemRepository itemRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private ActionRepository actRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private StockRepository stRep;
	@Autowired private Criteria criteria;

	
	@GetMapping("instocks/{stid}")
	public List<Transaction> inStock(@PathVariable("stid") long stid,@PathVariable("depid") long depid){
		return criteria.inStocks(stid, depid);
	}
	@GetMapping("outstocks/{stid}")
	public List<Transaction> outStock(@PathVariable("stid") long stid){
		return criteria.outStocks(stid);
	}
	
	@GetMapping("testing")
	public List<Action> getActionType(@PathVariable long depid){
		return dms.getActionType("طلب اضافة", depid);
	}
	
	@GetMapping("/{act_type}/actiondetails")
	public List<ActionHolder> findAction(@PathVariable("act_type")String type, @PathVariable("depid") long depid) {
		if(type.equals("requests") | type.equals("refunds") )
		return dms.findAction(type,depid);
		if(type.equals("transactions"))
			return dms.findTransactionAction(depid);
		return new ArrayList<>();
	}
	
	@GetMapping("/requests/{actid}")
	public List<Request> getRequestDetails(@PathVariable("actid") long aid){
		return dms.getRequestDetails(aid);
	}
	
	@GetMapping("/refunds/{actid}")
	public List<Refund> getRefundDetails(@PathVariable("actid") long aid){
		return dms.getRefundDetails(aid);
	}
	
	@GetMapping("/transactions/{actid}")
	public List<Transaction> getTransactionDetails(@PathVariable("actid") long aid,@PathVariable("depid")long depid){
		return dms.getTransactionDetails(aid,depid);
	}
	
	@GetMapping("/refunds")
	public List<Action> getRefundActions(@PathVariable("depid") long uid) {
		return dms.getActionType("طلب استرجاع", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());
	}

	@GetMapping("/deprives")
	public List<Action> getDepriveActions(@PathVariable("depid") long uid) {
		return dms.getActionType("طلب جرد", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());

	}

	@PostMapping("/makerequest")
	public String makeRequestActions(@RequestBody Pair<RequestHolder> input,@PathVariable("depid")long uid) {
		return dms.makeRequestAction(input,uid);
	}

	@PostMapping("/makerefund")
	public String makeRefundActions(@RequestBody Pair<Tuple> input,@PathVariable("depid")long uid) {
		dms.makeRefundAction(input,uid);
		return "DONE";
	}

	@PostMapping("/deprives")
	public String makeDepriveActions() {
		// TALK WITH AHMED JONES
		return "DONE";
	}

	@GetMapping("/stocks")
	public List<Stock> getStocks(@PathVariable("depid") long uid) {
		return dms.getAllStocks(uid);
	}

	@GetMapping("/transactions")
	public List<Action> getTransactionActions(@PathVariable("depid") long uid) {
		return dms.getActionType("طلب تحويل", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());
	}
	
	@GetMapping("/items")
	public List<Item> getItems(){
		return itemRep.findByIsAvailable(true);
	}
	@GetMapping("signatures/{act_id}")
	List<Signature> getSignatures(@PathVariable("act_id") long aid){
		return signRep.findByAction(actRep.findById(aid).get());
	}
	
	@GetMapping("stock/{stid}")
	Stock getStock(@PathVariable("stid") long stid, @PathVariable("depid") long depid) {
		return stRep.findById(stid).get();
	}
	
	@GetMapping("/test")
	List<Transaction> getAllTransactions(@PathVariable("depid") long depid) {
		return criteria.getAllTransactions(depid);
	}
	
	@GetMapping("/test2/{stid}")
	List<Transaction> getStockTransactions(@PathVariable("depid") long depid,@PathVariable("stid") long stid) {
		return criteria.getStockTransactions(depid,stid);
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
