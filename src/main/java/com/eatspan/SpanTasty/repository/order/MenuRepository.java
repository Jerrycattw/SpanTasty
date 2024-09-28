package com.eatspan.SpanTasty.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.order.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	
	List<MenuEntity> findByFoodKind(String foodKind);
}
