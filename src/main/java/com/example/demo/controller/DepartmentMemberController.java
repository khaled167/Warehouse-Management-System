package com.example.demo.controller;

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
import com.example.demo.entity.Item;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RequestHolder;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.Tuple;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.DepartmentMemberService;

@RestController
@RequestMapping("dep_memb/{userId}")
@CrossOrigin("http://localhost:3000")

public class DepartmentMemberController {

	@Autowired private DepartmentMemberService dms;
	@Autowired private UserRepository userRep;
	@Autowired private ItemRepository itemRep;
	@Autowired private RoleRepository roleRep;

	
	@GetMapping("/requests")
	public List<Action> getRequestActions(@PathVariable("userId") long uid) {
		return dms.getActionType("طلب اضافة", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());
	}
	
	@GetMapping("/requests/{reqid}")
	public List<Request> getRequestDetails(@PathVariable("reqid") long aid){
		return dms.getRequestDetails(aid);
	}
	
	@GetMapping("/refunds/{refid}")
	public List<Refund> getRefundDetails(@PathVariable("refid") long aid){
		return dms.getRefundDetails(aid);
	}
	
	@GetMapping("/transactions/{transid}")
	public List<Transaction> getTransactionDetails(@PathVariable("transid") long aid){
		return dms.getTransactionDetails(aid);
	}
	
	@GetMapping("/refunds")
	public List<Action> getRefundActions(@PathVariable("userId") long uid) {
		return dms.getActionType("طلب استرجاع", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());
	}

	@GetMapping("/deprives")
	public List<Action> getDepriveActions(@PathVariable("userId") long uid) {
		return dms.getActionType("طلب جرد", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());

	}

	@PostMapping("/makerequest")
	public String makeRequestActions(@RequestBody Pair<RequestHolder> input,@PathVariable("userId")long uid) {
		return dms.makeRequestAction(input,uid);
	}

	@PostMapping("/refunds")
	public String makeRefundActions(@RequestBody Pair<Tuple> input,@PathVariable("userId")long uid) {
		dms.makeRefundAction(input,uid);
		return "DONE";
	}

	@PostMapping("/deprives")
	public String makeDepriveActions() {
		// TALK WITH AHMED JONES
		return "DONE";
	}

	@GetMapping("/stocks")
	public List<Stock> getStocks(@PathVariable("userId") long uid) {
		long id = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id();
		System.out.println(id);
		return dms.getAllStocks(id);
	}

	@GetMapping("/transactions")
	public List<Action> getTransactions(@PathVariable("userId") long uid) {
		return dms.getActionType("طلب تحويل", roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse().getWarehouse_id());
	}
	
	@GetMapping("/items")
	public List<Item> getItems(){
		return itemRep.findByIsAvailable(true);
	}
}
