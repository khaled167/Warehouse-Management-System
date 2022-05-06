package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Action;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

	@Query(value = "select * from actions where "
			+ "action_type = ?1 && action_id in "
			+ "(select action_id from signatures where user_id in "
			+ "(select user_id from roles where warehouse_id = ?2))",nativeQuery = true)
	public List<Action> getActionType(String actionType,long warehouseId);
	
	
	@Query(value = "(SELECT * FROM actions where action_type = ?1 order by action_date desc)",nativeQuery = true)
	public List<Action> getAllActions(String type);

	
	
	@Query(value = "select * from actions where "
			+ "action_id in "
			+ "(select action_id from signatures where user_id in "
			+ "(select user_id from roles where warehouse_id = ?1))",nativeQuery = true)
	public List<Action> getWarehouseAction(long whid);
	
	
	
	@Query(value = "select count(*) from actions where month(action_date) = ?1 && action_type = ?2",nativeQuery = true)
	int monthRequests(int month,String type);
	
	
}
