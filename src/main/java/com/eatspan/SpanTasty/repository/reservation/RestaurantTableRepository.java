package com.eatspan.SpanTasty.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, RestaurantTableId> {
	
	
	@Query("from RestaurantTable rt where rt.id.restaurantId = :id ORDER BY rt.id.tableTypeId, rt.id.tableId ")
	List<RestaurantTable> findRestaurantTableByRestaurantId(@Param("id") Integer restaurantId);
	
	// 查詢RestaurantTableId
	@Query("SELECT MAX(CAST(rt.id.tableId AS int)) FROM RestaurantTable rt WHERE rt.id.restaurantId = :restaurantId AND rt.id.tableTypeId = :tableTypeId")
    Integer findMaxTableIdByRestaurantAndType(@Param("restaurantId") Integer restaurantId, @Param("tableTypeId") String tableTypeId);
	

}
