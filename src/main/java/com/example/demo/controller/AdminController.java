package com.example.demo.controller;

import java.util.List;
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
import com.example.demo.entity.User;
import com.example.demo.entity.Warehouse;
import com.example.demo.repository.Holder;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WarehouseRepository;
import com.example.demo.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("http://localhost:3000")
public class AdminController {
	@Autowired AdminService adminService;
	@Autowired WarehouseRepository whRep;
	@Autowired ItemRepository itemRep;
	@Autowired UserRepository userRep;

	@GetMapping("/items")
	public ResponseEntity<List<Item>> readItems(){
		return adminService.readItems();
	}
	@PostMapping("/items")
	public ResponseEntity<Item> createItem(@RequestBody Item item){
		return adminService.createItem(item);
		
	}
	@GetMapping("/items/{id}")
	public ResponseEntity<Item> readItem(@PathVariable Long id){
		return adminService.readItem(id);
	}
	@DeleteMapping("/items/{id}")
	public ResponseEntity<HttpStatus> deleteItem(@PathVariable Long id){
		return adminService.deleteItem(id);

	}
	@PutMapping("/items")
	public ResponseEntity<Item> updateItem(@RequestBody Holder holder){
		return adminService.updateItem(holder);

	}
	@GetMapping("/deletedItems")
	public ResponseEntity<List<Item>> readDeletedItems(){
		return adminService.readDeletedItems();
	}
	@DeleteMapping("/deletedItems/{id}")
	public ResponseEntity<HttpStatus> restoreItem(@PathVariable Long id){
		return adminService.restoreItem(id);

	}
	
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> readUsers(){
		return adminService.readUsers();
	}
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user){
		return adminService.createUser(user);

	}
	@GetMapping("/users/{id}")
	public ResponseEntity<User> readUser(@PathVariable Long id){
		return adminService.readUser(id);
	}
	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id){
		return adminService.deleteUser(id);

	}
	@PutMapping("/users")
	public ResponseEntity<User> updateUser(@RequestBody Holder holder){
		return adminService.updateUser(holder);
		
	}
	@DeleteMapping("/deletedUsers/{id}")
	public ResponseEntity<HttpStatus> restoreUser(@PathVariable Long id){
		return adminService.restoreUser(id);

	}
	
	
	
	
	@GetMapping("/warehouses")
	public ResponseEntity<List<Warehouse>> readNotes(){
	
		return adminService.readWarehouses();
		}

	@PostMapping("/warehouses/")
	public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse){
		return adminService.createWarehouse(warehouse);
		
	}
	@GetMapping("/warehouses/{id}")
	public ResponseEntity<Warehouse> readWarehouse(@PathVariable Long id){
		return adminService.readWarehouse(id);
	}
	@DeleteMapping("/warehouses/{id}")
	public ResponseEntity<HttpStatus> deleteWarehouse(@PathVariable Long id){
		return adminService.deleteWarehouse(id);
	}
	@PutMapping("/warehouses")
	public ResponseEntity<Warehouse> updateWarehouse(@RequestBody Holder holder ){
		return adminService.updateWarehouse(holder);
		
	}
	@GetMapping("/deletedWarehouses")
	public ResponseEntity<List<Warehouse>> readDeletedWarehouses(){
		return adminService.readDeletedWarehouses();
	}
	
	@DeleteMapping("/deletedWarehouses/{id}")
	public ResponseEntity<HttpStatus> restoreWarehouse(@PathVariable Long id){
		return adminService.restoreWarehouse(id);
	}
	
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> readUsersRoles(){
		return adminService.readUsersRoles();
	}
	@PostMapping("/roles")
	public ResponseEntity<Role> creatUeserRole(@RequestBody Role role){
		return adminService.creatUeserRole(role);
		
	}
	@GetMapping("/roles/{id}")
	public ResponseEntity<Role> readUserRole(@PathVariable Long id){
		return adminService.readRole(id);
	}
	
	@PutMapping("/roles")
	public ResponseEntity<Role> updatRoleUser(@RequestBody Holder holder){
		return adminService.updatRoleUser(holder);	
	}
	
	@GetMapping("/deletedRoles")
	public ResponseEntity<List<Role>> readDeletedRoles(){
		return adminService.readDeletedRole();
	}
	@GetMapping("/getEditHistory/{name}/{id}")
	public ResponseEntity<List<AdminEditDetails>> getEditHistory(@PathVariable String name,@PathVariable long id){
		return adminService.getEditHistrory(name,id);
	}
	@GetMapping("/dets")
	public List<Long> getDets(){
		return List.of(whRep.countAllActiveWarehouse(),itemRep.countAllActiveItems(),userRep.countAllActiveUser());
	}
	
	}

