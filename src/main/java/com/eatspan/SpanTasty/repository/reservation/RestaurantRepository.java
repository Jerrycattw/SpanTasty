package com.eatspan.SpanTasty.repository.reservation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.reservation.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	
	
	// 在 RestaurantRepository 中新增此方法
	Page<Restaurant> findByRestaurantStatus(Integer restaurantStatus, Pageable pageable);

	List<Restaurant> findByRestaurantAddressContaining(String city);
	

}
