package com.eatspan.SpanTasty.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eatspan.SpanTasty.entity.order.FoodKindEntity;

public interface FoodKindRepository extends JpaRepository<FoodKindEntity, Integer> {
	
	List<FoodKindEntity> findByFoodKindName(String foodKindName);
	
	//Coupon使用 取得togoType選項
	@Query("SELECT f.foodKindName FROM FoodKindEntity f ")
	List<String> findFoodKindName();
	
}
