package com.eatspan.SpanTasty.service.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.repository.reservation.RestaurantRepository;
import com.eatspan.SpanTasty.repository.reservation.RestaurantTableRepository;
import com.eatspan.SpanTasty.repository.reservation.TableTypeRepository;

@Service
public class RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private TableTypeRepository tableTypeRepository;
	@Autowired
	private RestaurantTableRepository restaurantTableRepository;
	
	// 新增餐廳
	public Restaurant addRestaurant(Restaurant restaurant) {
		
	    if (restaurant.getRestaurantStatus() == null) {
	        restaurant.setRestaurantStatus(3);
	    }
		if(restaurant.getReservePercent() == null) {
			restaurant.setReservePercent(100); // 餐廳開放訂位的比例
		}
		if(restaurant.getReserveTimeScale() == null) {
			restaurant.setReserveTimeScale(30); // 訂位的區間(預設為30分鐘)
		}
		if(restaurant.getReserveMin() == null) {
			restaurant.setReserveMin(2); // 餐廳最少開放訂位的人數
		}
		if(restaurant.getReserveMax() == null) {
			restaurant.setReserveMax(10); // 餐廳最多開放訂位的人數
		}
	    
	    return restaurantRepository.save(restaurant); // 返回保存的餐廳對象
	    
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
	
	
	// 查詢所有餐廳(Page)
	public Page<Restaurant> findAllRestaurantsPage(Integer pageNumber, Integer itemNumber) {
		if(itemNumber == null) itemNumber=10;
		Pageable pageAble = PageRequest.of(pageNumber-1, itemNumber, Sort.Direction.DESC, "restaurantId");
		return restaurantRepository.findAll(pageAble);
	}
	
	// 查詢所有營業中餐廳(Page)
	public Page<Restaurant> findAllActiveRestaurantsPage(Integer pageNumber, Integer itemNumber) {
		if(itemNumber == null) itemNumber=10;
		Pageable pageAble = PageRequest.of(pageNumber-1, itemNumber, Sort.Direction.DESC, "restaurantId");
		return restaurantRepository.findByRestaurantStatus(1, pageAble);
	}
	
	
	//檢查前端回傳的訂位規則修改DTO物件
	


}
