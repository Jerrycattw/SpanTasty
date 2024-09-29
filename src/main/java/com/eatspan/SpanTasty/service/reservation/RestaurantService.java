package com.eatspan.SpanTasty.service.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.repository.reservation.RestaurantRepository;

@Service
public class RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	// 新增餐廳
	public Restaurant addRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}
	
	
	// 刪除餐廳
	public void deleteRestaurant(Integer restaurantId) {
		restaurantRepository.deleteById(restaurantId);
	}
	
	
	// 更新餐廳
	public Restaurant updateRestaurant(Restaurant restaurant) {
		Optional<Restaurant> optional = restaurantRepository.findById(restaurant.getRestaurantId());
		if(optional.isPresent()) {
			return restaurantRepository.save(restaurant);
		}
		return null;
	}
	
	
	// 查詢餐廳byId
	public Restaurant findRestaurantById(Integer restaurantId) {
		return restaurantRepository.findById(restaurantId).orElse(null);
	}

	
	// 查詢所有餐廳
	public List<Restaurant> findAllRestaurants() {
		return restaurantRepository.findAll();
	}
	

}
