package com.eatspan.SpanTasty.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eatspan.SpanTasty.dto.order.MenuDTO;
import com.eatspan.SpanTasty.entity.order.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	
	List<MenuEntity> findByFoodKindId(Integer foodKindId);
	
	List<MenuEntity> findFoodsByFoodNameContaining(String foodName);
	
}
