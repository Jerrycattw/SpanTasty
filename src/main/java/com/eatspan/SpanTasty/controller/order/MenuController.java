package com.eatspan.SpanTasty.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.eatspan.SpanTasty.entity.order.MenuEntity;
import com.eatspan.SpanTasty.service.order.MenuService;

@RestController
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/menu")
	public List<MenuEntity> getAllFoods(){
		return menuService.getAllFoods();
	}
	
	@GetMapping("/menu/{foodId}")
	public MenuEntity getFoodById(@PathVariable Integer foodId) {
		return menuService.getMenuById(foodId);
	}
	
	@GetMapping("/menu/{foodKind}")
	public List<MenuEntity> getFoodsByKind(@PathVariable String foodKind) {
		return menuService.getFoodsByKind(foodKind);
	}
	
	
	
}
