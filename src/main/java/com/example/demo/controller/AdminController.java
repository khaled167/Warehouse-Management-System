package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.AdminEditDetails;
import com.example.demo.entity.Item;
import com.example.demo.entity.Role;
import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.entity.Warehouse;
import com.example.demo.query.Criteria;
import com.example.demo.repository.EditHolder;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.FWKeeperService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:3000")
public class AdminController {
	@Autowired
	AdminService adminService;
	@Autowired
	WarehouseRepository whRep;
	@Autowired
	ItemRepository itemRep;
	@Autowired
	UserRepository userRep;
	@Autowired 	TransactionRepository transRep;
	@Autowired
	Criteria criteria;
	@Autowired FWKeeperService fwk;
	private int x = 0;

	@GetMapping("/preprocess")
	public void preprocess() {
		for(Item it : itemRep.findAll()) {
			List<Transaction> list = transRep.findByStockItem(it);
			double res = 0;
			int size = list.size();
			System.out.println("----------------------------------");
			for(Transaction t : list) {
				int dif = fwk.difference(t.getAction().getActionDate(), t.getRequest().getAction().getActionDate());
				System.out.println(dif);
				System.out.println(t.getQuantity());
				if(dif > 0)
				res += (1.0*t.getQuantity() / dif);
				else size--;
			}
			if(size > 0) 
				it.setUnitDeliveredTime(res/size);
			it.setNumberOfTransactions(size);
			itemRep.save(it);
			
		}
	}
	
	public String rand() {
		Random r = new Random();
		String val = "";
		for(int i = 0;i<15;i++) 
			val += (char)(r.nextInt(0, 27) +'a');
		return val;
	}
	@GetMapping("/data")
	public void data() {
		Random r = new Random();
		String whs[] = new String[] {"مخزن الشاطبي","مخزن الكيمياء","مخزن الرياضة","مخزن الفيزياء"};
		for(int i = 0;i<3000;i++) {
			String itemName = rand();
			String category = whs[r.nextInt(0,4)];
			String unit = rand();
			String description = rand();
			boolean isAvailable = r.nextBoolean();
			boolean isConsumable = r.nextBoolean();
			itemRep.save(new Item(itemName,category,unit,description,isAvailable,isConsumable));
		}
		
		
	}
	
	@GetMapping("/clicked")
	public int click() {
		System.out.println(x);
		return x;
	}
	@GetMapping("/helloworld")
	public String hello() {
		return adminService.makeFirstUser();
	}
	
	@PostMapping("/itemcat")
	public List<Item> getCategoryItems(@RequestBody Object category){

//		category = category.substring(1,category.length()-1);
		
		System.out.println(category);
		return itemRep.findByCategory(category+"");
	}
//	@GetMapping("/test/{actid}")
//	public Long test(@PathVariable long actid) {
//		return criteria.getWarehouse(actid);
//	}
	@GetMapping("/items")
	public ResponseEntity<List<Item>> readItems() {
		return adminService.readItems();
	}

	@PostMapping("/items")
	public ResponseEntity<Item> createItem(@RequestBody Item item) {
		return adminService.createItem(item);

	}

	@GetMapping("/items/{id}")
	public ResponseEntity<Item> readItem(@PathVariable Long id) {
		return adminService.readItem(id);
	}

	@DeleteMapping("/items/{id}")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id) {
		return adminService.deleteItem(id);

	}

	@PutMapping("/items")
	public ResponseEntity<Item> updateItem(@RequestBody EditHolder holder) {
		return adminService.updateItem(holder);

	}

	@GetMapping("/deletedItems")
	public ResponseEntity<List<Item>> readDeletedItems() {
		return adminService.readDeletedItems();
	}

	@PutMapping("/deletedItems/{id}")
	public ResponseEntity<HttpStatus> restoreItem(@PathVariable Long id) {
		return adminService.restoreItem(id);

	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> readUsers() {
		return adminService.readUsers();
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		return adminService.createUser(user);

	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> readUser(@PathVariable Long id) {
		return adminService.readUser(id);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
		return adminService.deleteUser(id);

	}

	@PutMapping("/users")
	public ResponseEntity<User> updateUser(@RequestBody EditHolder holder) {
		return adminService.updateUser(holder);

	}

	@PutMapping("/deletedUsers/{id}")
	public ResponseEntity<HttpStatus> restoreUser(@PathVariable Long id) {
		return adminService.restoreUser(id);

	}

	@GetMapping("/warehouses")
	public ResponseEntity<List<Warehouse>> readNotes() {
//		return new ResponseEntity<List<Warehouse>>(whRep.findAll(),HttpStatus.OK);
		return adminService.readWarehouses();
	}

	@PostMapping("/warehouses/")
	public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
		return adminService.createWarehouse(warehouse);

	}

	@GetMapping("/warehouses/{id}")
	public ResponseEntity<Warehouse> readWarehouse(@PathVariable Long id) {
		return adminService.readWarehouse(id);
	}

	@DeleteMapping("/warehouses/{id}")
	public ResponseEntity<HttpStatus> deleteWarehouse(@PathVariable Long id) {
		return adminService.deleteWarehouse(id);
	}

	@PutMapping("/warehouses")
	public ResponseEntity<Warehouse> updateWarehouse(@RequestBody EditHolder holder) {
		return adminService.updateWarehouse(holder);

	}

	@GetMapping("/deletedWarehouses")
	public ResponseEntity<List<Warehouse>> readDeletedWarehouses() {
		return adminService.readDeletedWarehouses();
	}

	@PutMapping("/deletedWarehouses/{id}")
	public ResponseEntity<HttpStatus> restoreWarehouse(@PathVariable Long id) {
		return adminService.restoreWarehouse(id);
	}

	@GetMapping("/roles")
	public ResponseEntity<List<Role>> readUsersRoles() {
		return adminService.readUsersRoles();
	}

	@PostMapping("/roles")
	public ResponseEntity<Role> creatUeserRole(@RequestBody Role role) {
		return adminService.creatUeserRole(role);

	}

	@GetMapping("/roles/{id}")
	public ResponseEntity<Role> readUserRole(@PathVariable Long id) {
		return adminService.readRole(id);
	}

	@PutMapping("/roles")
	public ResponseEntity<Role> updatRoleUser(@RequestBody EditHolder holder) {
		return adminService.updatRoleUser(holder);
	}

	@GetMapping("/deletedRoles")
	public ResponseEntity<List<Role>> readDeletedRoles() {
		return adminService.readDeletedRole();
	}

	@GetMapping("/getEditHistory/{name}/{id}")
	public ResponseEntity<List<AdminEditDetails>> getEditHistory(@PathVariable String name, @PathVariable long id) {
		return adminService.getEditHistrory(name, id);
	}

	@GetMapping("/dets")
	public List<Long> getDets() {
		return Arrays.asList((long) whRep.findByIsAvailableAndWarehouseNameNot(true,"لايوجد").size(), (long) itemRep.findByIsAvailable(true).size(),
				(long) userRep.findByIsAvailable(true).size());
	}

	@GetMapping("/mainwarehouses")
	public List<Warehouse> getMainWarehouses() {
		return adminService.findByWarehouseType("رئيسي");
	}
	
	@GetMapping("/branchedwarehouses")
	public List<Warehouse> getBranchedWarehouses() {
		return adminService.findByWarehouseType("فرعي");
	}
}
