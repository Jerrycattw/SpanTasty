package com.eatspan.SpanTasty.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatspan.SpanTasty.entity.order.FoodKindEntity;
import com.eatspan.SpanTasty.entity.order.MenuEntity;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.order.FoodKindService;
import com.eatspan.SpanTasty.service.order.MenuService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

@Controller
@RequestMapping("/StarCups")
public class StarCupsOrderController {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private FoodKindService foodKindService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	//前台新增訂單
	@GetMapping("/order")
	public String starcupsAddTogoPage(Model model) {
		List<MenuEntity> foodList = menuService.getAllFoods();
		List<FoodKindEntity> foodKindList = foodKindService.getAllFoodKind();
		model.addAttribute("foodList", foodList);
		model.addAttribute("foodKindList", foodKindList);
		return "starcups/order/addTogo";
	}
	
	//前台菜單展示
	@GetMapping("/menu")
	public String getAllMenu(Model model) {
		List<MenuEntity> foodList = menuService.getAllFoods();
		List<FoodKindEntity> foodKindList = foodKindService.getAllFoodKind();
		model.addAttribute("foodList", foodList);
		model.addAttribute("foodKindList", foodKindList);
		return "starcups/order/showMenu";
	}
	
	//購物車
	@GetMapping("/order/cart")
	public String togoCartPage(Model model) {
		List<MenuEntity> foodList = menuService.getAllFoods();
		model.addAttribute("foodList", foodList);
		return "starcups/order/togoCart";
	}
	
	//結帳畫面
	@GetMapping("/order/checkout")
	public String togoCheckoutPage(Model model) {
		List<Restaurant> restaurantList = restaurantService.findAllRestaurants();
		model.addAttribute("restaruantList", restaurantList);
		return "starcups/order/togoCheckout";
	}
	
}
