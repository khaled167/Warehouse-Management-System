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
import com.example.demo.entity.Holder;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.Warehouse;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.SignatureRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.DeanService;
import com.example.demo.service.FWKeeperService;


@RestController
@RequestMapping("/dean/{deanid}")
@CrossOrigin("http://localhost:3000")

public class DeanController {
	@Autowired private RefundRepository refRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private RequestRepository reqRep;
	@Autowired private FWKeeperService fwkServ;
	@Autowired private DeanService ds;
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository uRep;
	@Autowired private SignatureRepository signRep;
	@Autowired private DeanService deanServ;
	@Autowired private WarehouseRepository whRep;
	
	@GetMapping("/{act_type}")
	public List<Action> getAction(@PathVariable("act_type") String type){
		if(type.equals("requests"))
			return fwkServ.getActions("طلب اضافة");
		if(type.equals("refunds"))
			return fwkServ.getActions("طلب ارتجاع");
		if(type.equals("deprives"))
			return fwkServ.getActions("طلب جرد");
		if(type.equals("transactions"))
			return fwkServ.getActions("طلب تحويل");
		return new ArrayList<>();
	}
	
	@GetMapping("/requests/{act_id}")
	public List<Request> getAllRequest(@PathVariable("act_id") long aid){
		return reqRep.getAllRequestDetails(aid);
	}
	
	@GetMapping("/refunds/{act_id}")
	public List<Refund> getAllRefund(@PathVariable("act_id") long aid){
		return refRep.getAllRefundDetails(aid);
	}
	
	@GetMapping("/transactions/{act_id}")
	public List<Transaction> getAllTransaction(@PathVariable("act_id") long aid){
		return transRep.getAllTransactionDetails(aid);
	}
	
	@PostMapping("/maketransactions")
	public String makeTransaction(@RequestBody Holder input,@PathVariable("deanid") long uid) {
//		return fwkServ.makeTransaction(input, uid);
		return deanServ.makeTransaction(input,uid);
	}
	
	@RequestMapping("/{warehouseid}/actions")
	public List<Action> getWarehouseActions(@PathVariable("warehouseid") long whid){
		return ds.getWarehouseActions(whid);
	}
	
	@RequestMapping("/{warehouseid}/actions/{actiontype}")
	public List<Action> getSpecificAction(@PathVariable("warehouseid") long whid,@PathVariable("actiontype")String type){
		return actRep.getActionType(type, whid);
	}
	@RequestMapping("/{warehouseid}/actions/requests/{reqid}")
	public List<Request> getRequestDetails(@PathVariable("reqid")long aid){
		return reqRep.getAllRequestDetails(aid);
	}
	
	@RequestMapping("/{warehouseid}/actions/refunds/{refid}")
	public List<Refund> getRefundDetails(@PathVariable("reqid")long aid){
		return refRep.getAllRefundDetails(aid);
	}
	@RequestMapping("/{warehouseid}/actions/transactions/{transid}")
	public List<Transaction> getTransactionDetails(@PathVariable("reqid")long aid){
		return transRep.getAllTransactionDetails(aid);
	}
	@PostMapping("/requests/{actid}")
	public String setAllowedQuantity(@RequestBody List<Request> list,@PathVariable("actid") long aid,@PathVariable("deanid") long did) {
		Date date = new Date(System.currentTimeMillis());
		Signature sign = new Signature(actRep.findById(aid).get(),uRep.findById(did).get(),date,date) ;
		signRep.save(sign);
		reqRep.saveAll(list);
		return "DONE";
	}
	
	@GetMapping("/stats")
	public List<List<Integer>> actionPerMonth(){
		String arr[] = {"طلب اضافة","طلب تحويل","طلب استرجاع","تحويل طلب من عميد","طلب جرد"};
		List<List<Integer>> ret = new ArrayList<>();
		for(int i = 0;i<5;i++) ret.add(new ArrayList<Integer>());
		for(int i = 1;i<=12;i++) {
			ret.get(0).add(actRep.monthRequests(i,arr[0]));
			ret.get(1).add(actRep.monthRequests(i,arr[1]));
			ret.get(2).add(actRep.monthRequests(i,arr[2]));
			ret.get(3).add(actRep.monthRequests(i,arr[3]));
			ret.get(4).add(actRep.monthRequests(i,arr[4]));
		}
		return ret;
	}
	
	@GetMapping("/warehouse/{act_id}")
	public  Warehouse getWarehouse(@PathVariable("act_id")long actid) {
		return whRep.findById(whRep.getWarehouse(actid)).get();
	}
		
}