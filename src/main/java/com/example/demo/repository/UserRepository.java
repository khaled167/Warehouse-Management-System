package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.demo.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value=" SELECT * FROM users u WHERE u.is_available = 1", nativeQuery = true)
	List<User> findAllActiveUser();
	
	@Query(value=" SELECT count(*) FROM users u WHERE u.is_available = 1", nativeQuery = true)
	long countAllActiveUser();

	
	@Query(value = "SELECT * FROM users u WHERE u.username = ?1", nativeQuery = true)
	User findUserByUserName(String username);
	   
	@Query(value = "SELECT warehouse_id from roles where user_id = ?1 order by date_of_assign DESC limit 1",nativeQuery = true)
	long getWarehouseId(long id);
	
}
