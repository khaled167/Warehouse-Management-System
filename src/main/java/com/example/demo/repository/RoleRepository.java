package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
	@Query(value="SELECT * FROM roles r WHERE r.user_id IN"
			+ "( SELECT user_id FROM users u WHERE u.is_available=1)", nativeQuery = true)
	List<Role> findAllActiveUser();

	@Query(value="SELECT * FROM roles r WHERE r.user_id IN"
			+ "( SELECT user_id FROM users u WHERE u.is_available=0)", nativeQuery = true)
	List<Role> findAllDeletedUser();

}
