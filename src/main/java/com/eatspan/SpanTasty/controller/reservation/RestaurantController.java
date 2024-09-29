package com.eatspan.SpanTasty.controller.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/restaurant")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	
	
    // 新增餐廳
    @PostMapping("/add")
    public ResponseEntity<?> addRestaurant(@RequestBody Restaurant addRestaurant) {
        Restaurant restaurant = restaurantService.addRestaurant(addRestaurant);
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    // 修改餐廳
    @PutMapping("/set")
    public ResponseEntity<?> updateRestaurant(@RequestBody Restaurant setRestaurant) {
        Restaurant restaurant = restaurantService.updateRestaurant(setRestaurant);
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    // 刪除餐廳
    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable("id") Integer restaurantId) {
        if (restaurantService.findRestaurantById(restaurantId)==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// 餐廳不存在，返回 404
        }
        restaurantService.deleteRestaurant(restaurantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 刪除成功，無內容返回
    }
    
    // 查詢餐廳(byId)
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable("id") Integer restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        if (restaurant != null) {
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    // 查詢所有餐廳
    @GetMapping("/getAll")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        return new ResponseEntity<>(restaurants, HttpStatus.OK);
    }
	
	
	
	
	
}
