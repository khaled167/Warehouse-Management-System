package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Action;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

//	@Query(value = "select * from actions where "
//			+ "action_type = ?1 and action_id in "
//			+ "(select action_id from signatures where user_id in "
//			+ "(select user_id from roles where warehouse_id = ?2))",nativeQuery = true)
//	public List<Action> getActionType(String actionType,long warehouseId);
	
	
	List<Action> findByActionTypeOrderByActionDateDesc(String actionType);
	
	
//	@Query(value = "select * from actions where "
//			+ "action_id in "
//			+ "(select action_id from signatures where user_id in "
//			+ "(select user_id from roles where warehouse_id = ?1))",nativeQuery = true)
//	public List<Action> getWarehouseAction(long whid);
	
	
	List<Action> findByActionType(String actionType);	
	
	
	@Query(value = "SELECT DATEDIFF(?1, ?2)",nativeQuery = true)
	int getDateDif(Date d1,Date d2);
	
	
}	
