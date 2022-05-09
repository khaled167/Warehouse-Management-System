package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

public interface RoleRepository extends JpaRepository<Role, Long>{
	@Query(value="SELECT * FROM roles r WHERE r.user_id IN"
			+ "( SELECT user_id FROM users u WHERE u.is_available=true)", nativeQuery = true)
	List<Role> findAllActiveUser();

	@Query(value="SELECT * FROM roles r WHERE r.user_id IN"
			+ "( SELECT user_id FROM users u WHERE u.is_available=false)", nativeQuery = true)
	List<Role> findAllDeletedUser();
	
	
	Role findTopByUserOrderByDateOfAssignDesc(User user);


}
