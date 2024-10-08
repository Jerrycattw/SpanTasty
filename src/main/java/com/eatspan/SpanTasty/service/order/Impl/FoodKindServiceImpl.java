package com.eatspan.SpanTasty.service.order.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.order.FoodKindEntity;
import com.eatspan.SpanTasty.repository.order.FoodKindRepository;
import com.eatspan.SpanTasty.service.order.FoodKindService;

@Service
public class FoodKindServiceImpl implements FoodKindService {
	
	@Autowired
	private FoodKindRepository foodKindRepositoroy;

	@Override
	public List<FoodKindEntity> getAllFoodKind() {
		return foodKindRepositoroy.findAll();
	}
	
	@Override
	public FoodKindEntity getFoodKindById(Integer foodKindId) {
		Optional<FoodKindEntity> optional = foodKindRepositoroy.findById(foodKindId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	@Override
	public FoodKindEntity addFoodKind(String foodKindName) {
		try {
			foodKindName = foodKindName.trim();
			FoodKindEntity foodKind = new FoodKindEntity();
			foodKind.setFoodKindName(foodKindName);
			return foodKindRepositoroy.save(foodKind);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}



