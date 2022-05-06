package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.AdminEditDetails;

public interface AdminEditDetailsRepository extends JpaRepository<AdminEditDetails, Long> {
	 @Query(value="SELECT * FROM admin_edit_details a WHERE a.admin_action_id in "
	 		+ "(SELECT admin_action_id from admin_actions where action_on_id = ?1 && action_entity = ?2)", nativeQuery = true)
		List<AdminEditDetails> getEditHistory(long id,String entity);
}
