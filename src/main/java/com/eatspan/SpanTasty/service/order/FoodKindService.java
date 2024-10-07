package com.eatspan.SpanTasty.service.order;

import java.util.List;

import com.eatspan.SpanTasty.entity.order.FoodKindEntity;

public interface FoodKindService {
	
	// 查詢
	public List<FoodKindEntity> getAllFoodKind();
	public FoodKindEntity getFoodKindById(Integer foodKindId);
	
	// 新增
	public FoodKindEntity addFoodKind(String foodKindName);
	
	
	
}
