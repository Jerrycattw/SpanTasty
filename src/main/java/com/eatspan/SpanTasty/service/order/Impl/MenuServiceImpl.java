package com.eatspan.SpanTasty.service.order.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.order.MenuEntity;
import com.eatspan.SpanTasty.repository.order.MenuRepository;
import com.eatspan.SpanTasty.service.order.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<MenuEntity> getAllFoods() {
		return menuRepository.findAll();
	}

	@Override
	public MenuEntity getMenuById(Integer foodId) {
		Optional<MenuEntity> optional = menuRepository.findById(foodId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<MenuEntity> getFoodsByKind(String foodKind) {
		return menuRepository.findByFoodKind(foodKind);
	}
	
	
	
}
