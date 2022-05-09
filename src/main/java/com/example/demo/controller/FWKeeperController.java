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
import com.example.demo.entity.ExaminationParam;
import com.example.demo.entity.Pair;
import com.example.demo.entity.Refund;
import com.example.demo.entity.Request;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.ActionRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RefundRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FWKeeperService;

@RestController
@RequestMapping("fwk/{fwkid}")
@CrossOrigin("http://localhost:3000")

public class FWKeeperController {
	
	@Autowired private FWKeeperService fwkServ;
	@Autowired private RequestRepository reqRep;
	@Autowired private RefundRepository refRep;
	@Autowired private TransactionRepository transRep;
	@Autowired private StockRepository stRep;
	@Autowired private ActionRepository actRep;
	@Autowired private UserRepository userRep;
	@Autowired private RoleRepository roleRep;
	@Autowired private ItemRepository itRep;

	
	
	@GetMapping("/{act_type}")
	public List<Action> getAction(@PathVariable("act_type") String type){
		if(type.equals("requests"))
			return fwkServ.getActions("طلب اضافة");
		if(type.equals("refunds"))
			return fwkServ.getActions("طلب ارتجاع");
		if(type.equals("deprives"))
			return fwkServ.getActions("طلب جرد");
		return fwkServ.getActions("طلب تحويل");
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
	
	@PostMapping("/maketransactions")
	public String makeTransaction(@RequestBody Pair<Transaction> input,@PathVariable("fwkid") long uid) {
		return fwkServ.makeTransaction(input, uid);
	}
	
	@PostMapping("/examine")
	public String makeExamine(@RequestBody ExaminationParam param,@PathVariable("fwkid") long uid) {
		return fwkServ.makeExamine(param,uid);
	}
	@GetMapping("/getitemstock/{itid}")
	public List<Stock> getItemStocks(@PathVariable("itid")long itid,@PathVariable("fwkid")long fwid){
		return stRep.findByItemAndWarehouseAndStatus(itRep.findById(itid).get(), roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwid).get()).getWarehouse(),"مقبول");
	}
}
