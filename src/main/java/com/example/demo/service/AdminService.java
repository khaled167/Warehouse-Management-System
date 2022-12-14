package com.example.demo.service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.entity.AdminAction;
import com.example.demo.entity.AdminEditDetails;
import com.example.demo.entity.Item;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.Warehouse;
import com.example.demo.query.Criteria;
import com.example.demo.repository.AdminActionRepository;
import com.example.demo.repository.AdminEditDetailsRepository;
import com.example.demo.repository.EditHolder;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;

@Component
@Service
public class AdminService {

	@Autowired private ItemRepository iRepo;
	@Autowired private UserRepository uRepo;
	@Autowired private WarehouseRepository wRepo;
	@Autowired private RoleRepository rRepo;
	@Autowired private AdminActionRepository adRepo;
	@Autowired private AdminEditDetailsRepository adeRepo;
	@Autowired private Criteria criteria;
	@Autowired EntityManager em;
	
	public String makeFirstUser(){
        long millis=System.currentTimeMillis();  
		User admin =new User("احمد محمد","احمد999","20405060","109 شارع","0124599","ahmed@example.com",new Date(millis),0,true,"28888");
		Warehouse ware=new Warehouse("لايوجد","لايوجد","لايوجد","لايوجد","لايوجد",new Date(millis),true);
		uRepo.save(admin);
		wRepo.save(ware);
		rRepo.save(new Role(admin,ware,1500,"ادمن",new Date(millis),new Date(millis)));
		
	
		return "done";
	}
	public ResponseEntity<List<Item>> readItems(){
        return new ResponseEntity<List<Item>>(iRepo.findByIsAvailable(true),HttpStatus.OK);
	}
	public ResponseEntity<Item> createItem(Item item){
        long millis=System.currentTimeMillis();  
		Date date = new Date(millis);
		// TODO CHANGE THE ADMIN ID WHEN CODING LOGIN
		User user=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(user,"اضافة","صنف",item.getItem_id(),item.getItemName(),date);
	    adRepo.save(adminAction);
		return new ResponseEntity<Item>(iRepo.save(item),HttpStatus.CREATED);
		
	}
	public ResponseEntity<Item> readItem(Long id){
		return new ResponseEntity<Item>(iRepo.findById(id).get(),HttpStatus.OK);
	}
	public ResponseEntity<HttpStatus> deleteItem(Long id){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			Item deletedItem= iRepo.findById(id).get();
		deletedItem.setAvailable(false);
		// TODO CHANGE THE ADMIN ID WHEN CODING LOGIN
		User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"ازالة","صنف",deletedItem.getItem_id(),deletedItem.getItemName(),date);
		adRepo.save(adminAction);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	public ResponseEntity<Item> updateItem(EditHolder holder){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			User admin=uRepo.findById((long) 1).get();
		Item item=iRepo.findById(holder.getId()).get();
		AdminAction adminAction=new AdminAction
		(admin,"تعديل","صنف",item.getItem_id(),item.getItemName(),date);
	    AdminEditDetails editDetails=null;
	    switch(holder.getEditType()){
	    case "اسم الصنف":
		    editDetails =new AdminEditDetails
		    (adminAction,holder.getEditType(),iRepo.getById(item.getItem_id()).getItemName());
		    item.setItemName(holder.getNewValue());
		    adminAction.setAction_on(holder.getNewValue());
	    	       break; 
	    case "الوحدة":    
	    	editDetails =new AdminEditDetails
	    	(adminAction,holder.getEditType(),iRepo.getById(item.getItem_id()).getUnit());
	    	item.setUnit(holder.getNewValue());
	    	    break; 
	   	case "الفئة":    
			editDetails =new AdminEditDetails
	        (adminAction,holder.getEditType(),iRepo.getById(item.getItem_id()).getCategory());
			item.setCategory(holder.getNewValue());
		    	 break; 
		case "الوصف":    
		editDetails =new AdminEditDetails
        (adminAction,holder.getEditType(),iRepo.getById(item.getItem_id()).getDescription());
		item.setDescription(holder.getNewValue());
	    	 break; 
	    }
	    adRepo.save(adminAction);
	    adeRepo.save(editDetails);
		return new ResponseEntity<Item>(iRepo.save(item),HttpStatus.OK);
		
	}
	public ResponseEntity<List<Item>> readDeletedItems(){
		return new ResponseEntity<List<Item>>(iRepo.findByIsAvailable(false),HttpStatus.OK);
	}
	public ResponseEntity<HttpStatus> restoreItem(Long id){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			Item deletedItem= iRepo.findById(id).get();
		deletedItem.setAvailable(true);
		User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"استرجاع","صنف",deletedItem.getItem_id(),deletedItem.getItemName(),date);
		adRepo.save(adminAction);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	public ResponseEntity<List<User>> readUsers(){
		return new ResponseEntity<List<User>>(uRepo.findAll(),HttpStatus.OK);
	}
	public ResponseEntity<User> createUser(User user){
		User checkeduser=uRepo.findByUsername(user.getUsername());
		if(checkeduser!=null) {
			return new ResponseEntity<User>(uRepo.findByUsername(user.getUsername()),HttpStatus.OK);
			}else {
				 long millis=System.currentTimeMillis();  
					Date date = new Date(millis);
					User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"اضافة","مستخدم",user.getUser_id(),user.getFullname(),date);
	    adRepo.save(adminAction);
		return new ResponseEntity<User>(uRepo.save(user),HttpStatus.CREATED);
		
	}}
	public ResponseEntity<User> readUser(Long id){
		return new ResponseEntity<User>(uRepo.findById(id).get(),HttpStatus.OK);
	}
	public ResponseEntity<HttpStatus> deleteUser(Long id){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			User deletedUser= uRepo.findById(id).get();
		deletedUser.setAvailable(false);
		User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"ازالة","مستخدم",deletedUser.getUser_id(),deletedUser.getFullname(),date);
		adRepo.save(adminAction);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	public ResponseEntity<User> updateUser(EditHolder holder){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);	
			User admin=uRepo.findById((long) 1).get();
		User user=uRepo.findById(holder.getId()).get();
		AdminAction adminAction=new AdminAction
		(admin,"تعديل","مستخدم",user.getUser_id(),user.getFullname(),date);
	    AdminEditDetails editDetails=null;
	    switch(holder.getEditType()){
	    case "اسم المستخدم":
		    editDetails =new AdminEditDetails
		    (adminAction,holder.getEditType(),uRepo.getById(user.getUser_id()).getUsername());
		    user.setUsername(holder.getNewValue());
		    adminAction.setAction_on(holder.getNewValue());
	    	       break; 
	    case "اسم المستخدم الكامل":    
	    	editDetails =new AdminEditDetails
	    	(adminAction,holder.getEditType(),uRepo.getById(user.getUser_id()).getFullname());
	    	user.setFullname(holder.getNewValue());
	    	    break; 
	   	case "رقم الهاتف":    
			editDetails =new AdminEditDetails
	        (adminAction,holder.getEditType(),uRepo.getById(user.getUser_id()).getPhoneNumber());
			user.setPhoneNumber(holder.getNewValue());
		    	 break;
	   	case "العنوان":
		    editDetails =new AdminEditDetails
		    (adminAction,holder.getEditType(),uRepo.getById(user.getUser_id()).getAddress());
		    user.setAddress(holder.getNewValue());
		    adminAction.setAction_on(holder.getNewValue());
	    	       break; 
	    case "الرقم القومي":    
	    	editDetails =new AdminEditDetails
	    	(adminAction,holder.getEditType(),uRepo.getById(user.getUser_id()).getNationalNum());
	    	user.setNationalNum(holder.getNewValue());
	    	    break; 
	   	case "تاريخ الميلاد":    
			editDetails =new AdminEditDetails
	        (adminAction,holder.getEditType(),uRepo.getById(user.getUser_id()).getBirthday().toString());
			Date newDate = Date.valueOf(holder.getNewValue());
			user.setBirthday(newDate);
		    	 break; 
	    }
	    adRepo.save(adminAction);
	    adeRepo.save(editDetails);
		return new ResponseEntity<User>(uRepo.save(user),HttpStatus.OK);
		
	}
	public ResponseEntity<HttpStatus> restoreUser(Long id){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			User deletedUser= uRepo.findById(id).get();
		deletedUser.setAvailable(true);
		User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"استرجاع","مستخدم",deletedUser.getUser_id(),deletedUser.getFullname(),date);
		adRepo.save(adminAction);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	public ResponseEntity<List<Warehouse>> readWarehouses(){
		
		return new ResponseEntity<List<Warehouse>>(wRepo.findByIsAvailableAndWarehouseNameNot(true,"لايوجد"),HttpStatus.OK);
	}
	public ResponseEntity<Warehouse> createWarehouse(Warehouse warehouse){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);	
			User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"اضافة","مخزن",warehouse.getWarehouse_id(),warehouse.getWarehouseName(),date);
	    adRepo.save(adminAction);
		return new ResponseEntity<Warehouse>(wRepo.save(warehouse),HttpStatus.CREATED);
		
	}
	public ResponseEntity<Warehouse> readWarehouse(Long id){
		return new ResponseEntity<Warehouse>(wRepo.findById(id).get(),HttpStatus.OK);
	}
	public ResponseEntity<HttpStatus> deleteWarehouse(Long id){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			Warehouse deletedWarehouse= wRepo.findById(id).get();
			deletedWarehouse.setAvailable(false);
			User admin=uRepo.findById((long) 1).get();
			AdminAction adminAction=new AdminAction(admin,"ازالة","مخزن",deletedWarehouse.getWarehouse_id(),deletedWarehouse.getWarehouseName(),date);
			adRepo.save(adminAction);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	public ResponseEntity<Warehouse> updateWarehouse(EditHolder holder ){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			User admin=uRepo.findById((long) 1).get();
		Warehouse warehouse=wRepo.findById(holder.getId()).get();
		AdminAction adminAction=new AdminAction
		(admin,"تعديل","مخزن",warehouse.getWarehouse_id(),warehouse.getWarehouseName(),date);
	    AdminEditDetails editDetails=null;
	    switch(holder.getEditType()){
	    case "اسم المخزن":
		    editDetails =new AdminEditDetails
		    (adminAction,holder.getEditType(),wRepo.getById(warehouse.getWarehouse_id()).getWarehouseName());
		    warehouse.setWarehouseName(holder.getNewValue());
		    adminAction.setAction_on(holder.getNewValue());
	    	       break; 
	    case "مكان المخزن":    
	    	editDetails =new AdminEditDetails
	    	(adminAction,holder.getEditType(),wRepo.getById(warehouse.getWarehouse_id()).getLocation());
	    	warehouse.setLocation(holder.getNewValue());
	    	    break; 
	   	case "نوع المخزن":    
			editDetails =new AdminEditDetails
	        (adminAction,holder.getEditType(),wRepo.getById(warehouse.getWarehouse_id()).getWarehouseType());
			warehouse.setWarehouseType(holder.getNewValue());
		    	 break;
	   	case "وصف المخزن":    
			editDetails =new AdminEditDetails
	        (adminAction,holder.getEditType(),wRepo.getById(warehouse.getWarehouse_id()).getDescription());
			warehouse.setDescription(holder.getNewValue());
		    	 break; 
	    }
	    adRepo.save(adminAction);
	    adeRepo.save(editDetails);
		return new ResponseEntity<Warehouse>(wRepo.save(warehouse),HttpStatus.OK);
		
	}
	public ResponseEntity<List<Warehouse>> readDeletedWarehouses(){
		return new ResponseEntity<List<Warehouse>>(wRepo.findByIsAvailableAndWarehouseNameNot(false,"لايوجد"),HttpStatus.OK);
	}
	public ResponseEntity<HttpStatus> restoreWarehouse(Long id){
		 long millis=System.currentTimeMillis();  
			Date date = new Date(millis);
			Warehouse deletedWarehouse= wRepo.findById(id).get();
		deletedWarehouse.setAvailable(true);
		User admin=uRepo.findById((long) 1).get();
		AdminAction adminAction=new AdminAction(admin,"استرجاع","مخزن",deletedWarehouse.getWarehouse_id(),deletedWarehouse.getWarehouseName(),date);
		adRepo.save(adminAction);
	
	return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
}
	public ResponseEntity<List<Role>> readUsersRoles(){
		return new ResponseEntity<List<Role>>(criteria.findAllActiveUser(),HttpStatus.OK);
//		return new ResponseEntity<List<Role>>(rRepo.findAllActiveUser(),HttpStatus.OK);
	}
	public ResponseEntity<Role> creatUeserRole(Role role){
		if(role.getWarehouse()==null) {
			role.setWarehouse(wRepo.findById((long) 1).get());
		}
		if(role.getDateOfAssign() == null)
			role.setDateOfAssign(new Date(System.currentTimeMillis()));
		uRepo.save(role.getUser());
		return new ResponseEntity<Role>(rRepo.save(role),HttpStatus.CREATED);
		
		
	}
	public ResponseEntity<Role> readRole(Long id){
		return new ResponseEntity<Role>(rRepo.findById(id).get(),HttpStatus.OK);
	}
	public ResponseEntity<Role> updatRoleUser(EditHolder holder){
	    Role role=rRepo.findById(holder.getId()).get();
		if(holder.getEditType().equals("الدور")) {
			role.setRole(holder.getNewValue());
		}else {
			updateUser(holder);	
		}
		return new ResponseEntity<Role>(rRepo.save(role),HttpStatus.OK);
		
	}
	public ResponseEntity<List<Role>> readDeletedRole(){
		return new ResponseEntity<List<Role>>(criteria.findAllDeletedUser(),HttpStatus.OK);
//		return new ResponseEntity<List<Role>>(rRepo.findAllDeletedUser(),HttpStatus.OK);
	}
    public ResponseEntity<List<AdminEditDetails>> getEditHistrory(String name,long id){
    	if (name.equals("item")) 
    		return new ResponseEntity<List<AdminEditDetails>>(criteria.getEditHistory(id, "صنف"),HttpStatus.OK);
//    		return new ResponseEntity<List<AdminEditDetails>>(adeRepo.getEditHistory(id,"صنف"),HttpStatus.OK);

    	else if( name.equals("warehouse"))
    		return new ResponseEntity<List<AdminEditDetails>>(criteria.getEditHistory(id, "مخزن"),HttpStatus.OK);
//    		return new ResponseEntity<List<AdminEditDetails>>(adeRepo.getEditHistory(id,"مخزن"),HttpStatus.OK);

    		else if(name.equals("user"))
        		return new ResponseEntity<List<AdminEditDetails>>(criteria.getEditHistory(id, "مستخدم"),HttpStatus.OK);
//    			return new ResponseEntity<List<AdminEditDetails>>(adeRepo.getEditHistory(id,"مستخدم"),HttpStatus.OK);
    		
    		else return new ResponseEntity<List<AdminEditDetails>>(Arrays.asList(),HttpStatus.OK);
    	}
    	
    public List<Warehouse> findByWarehouseType(String warehouseType){
    	return wRepo.findByWarehouseType(warehouseType);
    }
    }
	

