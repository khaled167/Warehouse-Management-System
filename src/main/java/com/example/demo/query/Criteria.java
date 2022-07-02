package com.example.demo.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Action;
import com.example.demo.entity.AdminAction;
import com.example.demo.entity.AdminEditDetails;
import com.example.demo.entity.Examination;
import com.example.demo.entity.ExaminationCommittee;
import com.example.demo.entity.ExaminationMemberSignature;
import com.example.demo.entity.Refund;
import com.example.demo.entity.RefundExamination;
import com.example.demo.entity.Request;
import com.example.demo.entity.Role;
import com.example.demo.entity.Signature;
import com.example.demo.entity.Stock;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.entity.Warehouse;
import com.example.demo.repository.ExaminationCommitteeRepository;
import com.example.demo.repository.ExaminationMemberSignatureRepository;
import com.example.demo.repository.ExaminationRepository;
import com.example.demo.repository.RefundExaminationRepository;
import com.example.demo.repository.RequestRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
@Repository
public class Criteria {

	
	@Autowired EntityManager em;
	@Autowired RoleRepository roleRep;
	@Autowired UserRepository userRep;
	@Autowired StockRepository stRep;
	@Autowired TransactionRepository transRep;
	@Autowired RequestRepository reqRep;
	@Autowired private ExaminationRepository exRep;
	@Autowired private ExaminationMemberSignatureRepository examinationMemberSignatureRepo;
	@Autowired private ExaminationRepository examinationRepo;
	@Autowired private RefundExaminationRepository refexaminationRepo;
	@Autowired private ExaminationCommitteeRepository examinationCommitteeRepo;

	
	/*
	 * 
	 * select 
	 * 
	 * 
	 * 
	 * */
	/*
	 	SELECT warehouse_id from roles where user_id =
			  (SELECT user_id FROM signatures WHERE action_id = 83 order by submit_date ASC limit 1)
				and ((select actions.action_date from actions where actions.action_id = 83) between roles.date_of_assign and roles.date_of_resign)		
	 */

	/*
	 * 
	 * select * from stocks where item_id in list & warehouse_id = getwarehouse
	 * */
	
	/*
	 * 
	 * 
	 * select action_id where action_type == t7wel and action_id in (
	 * (select action_id from signatures where user_id in
	 * (Select user_id from role where warehouse_id == warehouse_id)))
	 * */
	// (83 , 96 , 182 , 189 , 190 , 191 , 192 , 193 , 194 , 195 , 204 , 205 , 215 , 216 , 86 , 187 , 188 , 213 , 214))
	
	
	/*	select transaction_id from transactions where request_id in
	 * (select request_id from requests where action_id in
	 * (select action_id from signature where role.warehousename = ? and actiontype = "tlb t7wel"))
	 * 
	 * */
	
	public List<Transaction> findTransactionsByRequest(long reqid){
		return transRep.findByRequest(reqRep.findById(reqid).get());
	}
	public List<Action> findWarehouseExaminationActions(long warehouseId,String type){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Action> SignQuery = cb.createQuery(Action.class);
		
		Root<Signature> signature = SignQuery.from(Signature.class);
		
		SignQuery.select(signature.get("action")).where(cb.and(cb.equal(signature.get("role").get("warehouse").get("warehouse_id"), warehouseId),
				cb.equal(signature.get("action").get("actionType"), type)));
		
		return em.createQuery(SignQuery).getResultList();

	}
	
	
	public List<ExaminationMemberSignature> getExaminationMembers(long examinationId) {
		return examinationMemberSignatureRepo.findByExaminationCommittee(examinationCommitteeRepo.findById(examinationId).get());
	}
	public List<ExaminationCommittee> getExaminations() {
		return examinationCommitteeRepo.findAll();
	}
	public List<Examination> getExaminationDetails(long examinationId) {
	
		return examinationRepo.findByExaminationCommittee(examinationCommitteeRepo.findById(examinationId).get());
	}
	
	public List<RefundExamination> getRefundExaminationDetails(long refexaminationId) {
		
		return refexaminationRepo.findByExaminationCommittee(examinationCommitteeRepo.findById(refexaminationId).get());
	}
	
	public List<Examination> getStockExaminations(long stid) {
		Stock stock = stRep.findById(stid).get();
		return exRep.findByStockItemAndStockEntryDateAndStockExpiredDateAndStockPriceAndStockStatus(
				stock.getItem(),stock.getEntryDate(),stock.getExpiredDate(),stock.getPrice(),stock.getStatus());
	}
	public List<Transaction> inStocks(long stid,long uid) {
		Stock stock = stRep.findById(stid).get();
		
		return transRep.findByWarehouseAndStockItemAndStockEntryDateAndStockExpiredDateAndStockPriceAndStockStatus
				(stock.getWarehouse(), stock.getItem(), stock.getEntryDate(), stock.getExpiredDate(), stock.getPrice(), stock.getStatus());
	}	
	
	public List<Transaction> outStocks(long stid) {
		return transRep.findByStock(stRep.findById(stid).get());
	}
	
	
	public List<Action> findTransactionAction(long uid) {
	
		Warehouse warehouse = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse();
		
		String warehouseName = warehouse.getWarehouseName();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Action> SignQuery = cb.createQuery(Action.class);
		
		Root<Signature> signature = SignQuery.from(Signature.class);
		
		SignQuery.select(signature.get("action")).where(cb.and(cb.equal(signature.get("role").get("warehouse").get("warehouseName"),warehouseName),
				cb.equal(signature.get("action").get("actionType"), "طلب اضافة")));

		
		CriteriaQuery<Request> ReqQuery = cb.createQuery(Request.class);
		
		Root<Request> request = ReqQuery.from(Request.class);
		
		ReqQuery.select(request).where(request.get("action").in(em.createQuery(SignQuery).getResultList()));
		
		CriteriaQuery<Action> TransQuery = cb.createQuery(Action.class);
		
		Root<Transaction> transaction = TransQuery.from(Transaction.class);
		
		TransQuery.select(transaction.get("action")).where(transaction.get("request").in(em.createQuery(ReqQuery).getResultList()));
		
		return em.createQuery(TransQuery.distinct(true)).getResultList();
		
	}	
	
	
	public List<Action> findRefundAction(long uid){
		
		Warehouse warehouse = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse();
		String warehouseName = warehouse.getWarehouseName();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Action> cq = cb.createQuery(Action.class);
		
		Root<Refund> refund = cq.from(Refund.class);
		
		cq.select(refund.get("action").get("action_id")).where(cb.equal(refund.get("transaction").get("request").get("item").get("category"), warehouseName)).distinct(true);
		
		CriteriaQuery<Action> cqa = cb.createQuery(Action.class);
		
		Root<Action> action = cqa.from(Action.class);
		
		cqa.select(action).where(action.get("action_id").in(em.createQuery(cq).getResultList())).orderBy(cb.desc(action.get("actionDate"))).distinct(true);

		return em.createQuery(cqa).getResultList();

	}
	
	public List<Action> findRequestAction(long uid){
		Warehouse warehouse = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(uid).get()).getWarehouse();
		String warehouseName = warehouse.getWarehouseName();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Action> cq = cb.createQuery(Action.class);
		
		Root<Request> request = cq.from(Request.class);
		
		cq.select(request.get("action").get("action_id")).where(cb.equal(request.get("item").get("category"), warehouseName)).distinct(true);
		
		CriteriaQuery<Action> cqa = cb.createQuery(Action.class);
		
		Root<Action> action = cqa.from(Action.class);
		
		cqa.select(action).where(action.get("action_id").in(em.createQuery(cq).getResultList())).orderBy(cb.desc(action.get("actionDate"))).distinct(true);

		return em.createQuery(cqa).getResultList();

	}
	
	public List<Transaction> getStockTransactions(long userId,long stid) {
		
		List<Transaction> allTransactions = getAllTransactions(userId);		

		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Transaction> transactions = cb.createQuery(Transaction.class);
		
		Root<Transaction> transaction = transactions.from(Transaction.class);
		
		Stock stock = stRep.findById(stid).get();
		
		Predicate p1 = cb.and(cb.equal(transaction.get("stock").get("item").get("item_id"),stock.getItem().getItem_id()));
		Predicate p2 = cb.and(cb.equal(transaction.get("stock").get("entryDate"),stock.getEntryDate()));
		Predicate p3 = cb.and(cb.equal(transaction.get("stock").get("expiredDate"),stock.getExpiredDate()));
		Predicate p4 = cb.and(cb.equal(transaction.get("stock").get("status"),stock.getStatus()));
		Predicate p5 = cb.and(cb.equal(transaction.get("stock").get("price"),stock.getPrice()));
		
		transactions.select(transaction).where(cb.and(p1,p2,p3,p4,p5,transaction.in(allTransactions)));
		
		
		return em.createQuery(transactions).getResultList();
	}
	public List<Transaction> getAllTransactions(long userId){
		
		long whid = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(userId).get()).getWarehouse().getWarehouse_id();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Transaction> transactions = cb.createQuery(Transaction.class);
		
		Root<Transaction> trans = transactions.from(Transaction.class);
		
		List<Long> list = new ArrayList<>();
		for(Action act : getActionType("طلب اضافة", whid))
			list.add(act.getAction_id());
		transactions.select(trans).where(trans.get("request").get("action").get("action_id").in(list));
		
		return em.createQuery(transactions).getResultList();

		
	}
	public List<Stock> getStocks(List<Long> list,long fwid) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Stock> uid = cb.createQuery(Stock.class);
		
		Root<Stock> stock = uid.from(Stock.class);
		
		long warehouse = roleRep.findTopByUserOrderByDateOfAssignDesc(userRep.findById(fwid).get()).getWarehouse().getWarehouse_id();
		
		uid.select(stock).where(cb.and(stock.get("item").get("item_id").in(list),cb.equal(stock.get("warehouse").get("warehouse_id"),warehouse)));
		
		return em.createQuery(uid).getResultList();

	}
	public long getLastUser(long actionId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> uid = cb.createQuery(Long.class);
		
		Root<Signature> signature = uid.from(Signature.class);
		
		uid.select(signature.get("role").get("user").get("user_id")).where(cb.equal(signature.get("action").get("action_id"),actionId)).orderBy(cb.desc(signature.get("submitDate")));
		
		return em.createQuery(uid).setFirstResult(0).setMaxResults(1).getSingleResult();
	}
	
	public long getUser(long actionId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		
		CriteriaQuery<Long> uid = cb.createQuery(Long.class);
		
		Root<Signature> signature = uid.from(Signature.class);
		
		uid.select(signature.get("role").get("user").get("user_id")).where(cb.equal(signature.get("action").get("action_id"),actionId)).orderBy(cb.asc(signature.get("submitDate")));
		
		return em.createQuery(uid).setFirstResult(0).setMaxResults(1).getSingleResult();
	}
//	public 	long getWarehouse(long actionId) {
//		// select role.warehouse_id from signature where action_id = actionId order by submit_date asc
//		
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		
//		CriteriaQuery<Long> uid = cb.createQuery(Long.class);
//		
//		Root<Signature> signature = uid.from(Signature.class);
//		
//		uid.select(null)
//		uid.select(signature.get("user").get("user_id")).where(cb.equal(signature.get("action").get("action_id"),actionId)).orderBy(cb.asc(signature.get("submit_date")));
//		
//		CriteriaQuery<Long> res = cb.createQuery(Long.class);
//		Root<Role> role = res.from(Role.class); 
//		
//		res.select(role.get("user").get("user_id")).where(cb.equal(role.get("user").get("user_id"),em.createQuery(uid).setFirstResult(0).setMaxResults(1).getSingleResult()));
//		CriteriaQuery<Date> ad = cb.createQuery(Date.class);
//		Subquery<Date> actdate = ad.subquery(Date.class);
//		Root<Action> action = actdate.from(Action.class);
//		actdate.select(action.get("actionDate")).where(cb.equal(action.get("action_id"),actionId));
//		res.select(role.get("warehouse").get("warehouse_id")).where(cb.equal(role.get("user").get("user_id"), em.createQuery(uid).setFirstResult(0).setMaxResults(1).getSingleResult())
//				,
//				cb.and(cb.or(cb.and( cb.greaterThanOrEqualTo(actdate,role.get("dateOfAssign")),cb.isNull(role.get("dateOfResign"))), cb.between(actdate, role.get("dateOfAssign"), role.get("dateOfResign"))))
//				);
//		
//		return em.createQuery(res).getSingleResult();
		
//	}	
	
	public List<Role> findAllDeletedUser(){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> uid = cb.createQuery(Long.class);
		Root<User> user = uid.from(User.class);
		uid.select(user.get("user_id")).where(cb.equal(user.get("isAvailable"),false));
		CriteriaQuery<Role> res = cb.createQuery(Role.class);
		Root<Role> role = res.from(Role.class);
		res.select(role).where(role.get("user").get("user_id").in(em.createQuery(uid).getResultList()));
		return em.createQuery(res).getResultList();

	}
	public 	List<Role> findAllActiveUser() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> uid = cb.createQuery(Long.class);
		Root<User> user = uid.from(User.class);
		uid.select(user.get("user_id")).where(cb.equal(user.get("isAvailable"),true));
		
		CriteriaQuery<Role> res = cb.createQuery(Role.class);
		Root<Role> role = res.from(Role.class);
		res.select(role).where(role.get("user").get("user_id").in(em.createQuery(uid).getResultList()));
		return em.createQuery(res).getResultList();
	}
	public List<AdminEditDetails> getEditHistory(long id,String entity){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> aaid = cb.createQuery(Long.class);
		Root<AdminAction> AdminAction = aaid.from(AdminAction.class);
		aaid.select(AdminAction.get("admin_action_id")).where(cb.equal(AdminAction.get("action_on_id"),id),cb.and(cb.equal(AdminAction.get("action_entity"),entity)));
		CriteriaQuery<AdminEditDetails> res = cb.createQuery(AdminEditDetails.class);
		Root<AdminEditDetails> adminEditDetails = res.from(AdminEditDetails.class);
		res.select(adminEditDetails).where(adminEditDetails.get("adminAction").get("admin_action_id").in(em.createQuery(aaid).getResultList()));
		return em.createQuery(res).getResultList();
	}
	public List<Action> getWarehouseAction(long whid){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> uid = cb.createQuery(Long.class);
		Root<Role> roles = uid.from(Role.class);
		
		uid.select(roles.get("user").get("user_id")).where(cb.equal(roles.get("warehouse").get("warehouse_id"),whid));

		
		CriteriaQuery<Long> aid = cb.createQuery(Long.class);
		Root<Signature> signature = aid.from(Signature.class);
		
		aid.select(signature.get("action").get("action_id")).where(signature.get("user").get("user_id").in(em.createQuery(uid).getResultList()));
		
		CriteriaQuery<Action> res = cb.createQuery(Action.class);
		Root<Action> action = res.from(Action.class);
		res.select(action).where(action.get("action_id").in(em.createQuery(aid).getResultList()));

		return em.createQuery(res).getResultList();

	}
	public List<Action> getActionType(String actionType, long whid) {
		
		// select action from signatures where role.warehouse_id = whid and action.action_type = actionType
		System.out.println(whid+" here");
		System.out.println(actionType);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Action> actions = cb.createQuery(Action.class);
		Root<Signature> signs = actions.from(Signature.class);
		actions.select(signs.get("action")).where(cb.and(cb.equal(signs.get("role").get("warehouse").get("warehouse_id"), whid),cb.equal(signs.get("action").get("actionType"), actionType)));
		return em.createQuery(actions).getResultList();
	}
}
