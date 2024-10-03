package com.eatspan.SpanTasty.repository.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, RestaurantTableId> {
	
	
	@Query("from RestaurantTable rt where rt.id.restaurantId = :id")
	List<RestaurantTable> findRestaurantTableByRestaurantId(@Param("id") Integer restaurantId);

}
