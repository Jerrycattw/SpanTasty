package com.eatspan.SpanTasty.service.order;

import java.util.List;

import com.eatspan.SpanTasty.entity.order.MenuEntity;

public interface MenuService {
	// 查詢
	public List<MenuEntity> getAllFoods();
	public MenuEntity getMenuById(Integer foodId);
	public List<MenuEntity> getFoodsByKind(String foodKind);
	
}
