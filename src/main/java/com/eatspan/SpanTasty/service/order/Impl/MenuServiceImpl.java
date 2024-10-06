package com.eatspan.SpanTasty.service.order.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.order.FoodKindEntity;
import com.eatspan.SpanTasty.entity.order.MenuEntity;
import com.eatspan.SpanTasty.repository.order.FoodKindRepository;
import com.eatspan.SpanTasty.repository.order.MenuRepository;
import com.eatspan.SpanTasty.service.order.MenuService;

@Service
public class MenuServiceImpl implements MenuService {
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private FoodKindRepository foodKindRepository;
	
//	@Override
//	public List<MenuDTO> getAllFoods() {
//		List<MenuEntity> menus = menuRepository.findAll();
//		List<MenuDTO> menuDtos = new ArrayList<>();
//		for(MenuEntity menu : menus ) {
//			MenuDTO menuDto = new MenuDTO();
//			menuDto.menuToMenuDto(menu);
//		}
//		return menuDtos;
//	}
	
	
	
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
	public List<MenuEntity> getFoodsByKind(Integer foodKindId) {
		return menuRepository.findByFoodKindId(foodKindId);
	}

	@Override
	public List<MenuEntity> getFoodsByFoodName(String foodName) {
		return menuRepository.findFoodsByFoodNameContaining(foodName);
	}
	

	@Override
	public MenuEntity addFood(MenuEntity newFood) {
//		System.out.println(newFood.getFoodKindId());
//		System.out.println(newFood.getFoodKind());
//		String newFoodKindName = newFood.getFoodKind().getFoodKindName();
//		System.out.println(newFoodKindName);
//		if (newFoodKindName != null) {
//	        // 依照種類名稱尋找
//	        List<FoodKindEntity> optional = foodKindRepository.findByFoodKindName(newFoodKindName);
//	        if (optional.isEmpty()) {
//	            // 不存在，加入新的FoodKind
//	            FoodKindEntity foodKind = new FoodKindEntity();
//	            foodKind.setFoodKindName(newFoodKindName);
//	            newFood.setFoodKind(foodKind); // 直接加入 newFood，cascade.persist
//	        } else {
//	            // 已存在，直接加入已存在的FoodKind
//	            newFood.setFoodKind(optional.get(0));
//	        }
//	    }
		try {
			MenuEntity savedFood = menuRepository.save(newFood);
			return savedFood;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteFoodById(Integer foodId) {
		menuRepository.deleteById(foodId);
	}
	
	@Transactional
	@Override
	public MenuEntity updateFoodById(Integer foodId, MenuEntity updateFood) {
		Optional<MenuEntity> optional = menuRepository.findById(foodId);
		if (optional.isPresent()) {
			MenuEntity food = optional.get();
			if (updateFood.getFoodName() != null) {
				food.setFoodName(updateFood.getFoodName());
			}
			if (updateFood.getFoodPicture() != null) {
				food.setFoodPicture(updateFood.getFoodPicture());
			}
			if (updateFood.getFoodPrice() != null) {
				food.setFoodPrice(updateFood.getFoodPrice());
			}
			if (updateFood.getFoodKindId() != null) {
				food.setFoodKindId(updateFood.getFoodKindId());
			}
			if (updateFood.getFoodStock() != null) {
				food.setFoodStock(updateFood.getFoodStock());
			}
			if (updateFood.getFoodDescription() != null) {
				food.setFoodDescription(updateFood.getFoodDescription());
			}
			if  (updateFood.getFoodStatus() != null) {
				food.setFoodStatus(updateFood.getFoodStatus());
			}
			return food;
		}
		return null;
	}

	
	
}
