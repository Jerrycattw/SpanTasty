package com.eatspan.SpanTasty.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.eatspan.SpanTasty.entity.order.MenuEntity;
import com.eatspan.SpanTasty.service.order.MenuService;

@RestController
public class MenuController {
	
	@Autowired
	private MenuService menuService;
	
	
	// 有參數：模糊查詢，無參數：查詢全部
	@GetMapping("/menu")
	public ResponseEntity<List<MenuEntity>> getFoods(@RequestParam(required = false) String foodName){
		if (foodName != null) {
			List<MenuEntity> foods = menuService.getFoodsByFoodName(foodName);
			if (foods.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT");
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok(foods); 
		}
		return ResponseEntity.ok(menuService.getAllFoods());
	}
	
	@GetMapping("/menu/{foodId}")
	public ResponseEntity<MenuEntity> getFoodById(@PathVariable Integer foodId) {
		return ResponseEntity.ok(menuService.getMenuById(foodId));
	}
	
	@GetMapping("/menu/kinds/{foodKindId}")
	public ResponseEntity<List<MenuEntity>> getFoodsByKind(@PathVariable Integer foodKindId) {
		return ResponseEntity.ok(menuService.getFoodsByKind(foodKindId));
	}
	
	@PostMapping("/menu")
	public ResponseEntity<List<MenuEntity>> addFood(@RequestBody MenuEntity newFood) {
		menuService.addFood(newFood);
		return ResponseEntity.ok(menuService.getAllFoods());
	}
	
	@DeleteMapping("/menu/{foodId}")
	public ResponseEntity<List<MenuEntity>> deleteFoodById(@PathVariable Integer foodId) {
		menuService.deleteFoodById(foodId);
		return ResponseEntity.ok(menuService.getAllFoods());
	}
	
	@PutMapping("/menu/{foodId}")
	public ResponseEntity<MenuEntity> updateFoodById(@PathVariable Integer foodId, @RequestBody MenuEntity updateFood) {
		return ResponseEntity.ok(menuService.updateFoodById(foodId, updateFood));
	}
	
}





