package com.eatspan.SpanTasty.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.order.FoodKindEntity;

public interface FoodKindRepository extends JpaRepository<FoodKindEntity, Integer> {
	
	List<FoodKindEntity> findByFoodKindName(String foodKindName);
	
}
