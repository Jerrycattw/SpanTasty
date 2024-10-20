package com.eatspan.SpanTasty.service.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;
import com.eatspan.SpanTasty.repository.reservation.RestaurantTableRepository;

@Service
public class RestaurantTableService {
	
	@Autowired
	private RestaurantTableRepository restaurantTableRepository;
	
	
	
	// 新增餐廳桌位
	public RestaurantTable addRestaurantTable(Integer restaurantId, String tableTypeId) {
        Integer maxTableId = restaurantTableRepository.findMaxTableIdByRestaurantAndType(restaurantId, tableTypeId);
        Integer newTableId = (maxTableId != null) ? maxTableId + 1 : 1;
        RestaurantTable newTable = new RestaurantTable(new RestaurantTableId(restaurantId, tableTypeId, newTableId));
		return restaurantTableRepository.save(newTable);
	}
	
	
	
	// 刪除餐廳桌位
	public void deleteRestaurantTable(RestaurantTableId restaurantTableId) {
		restaurantTableRepository.deleteById(restaurantTableId);
	}
	
	
	// 更新餐廳桌位
	public RestaurantTable updateRestaurantTable(RestaurantTable restaurantTable) {
		Optional<RestaurantTable> optional = restaurantTableRepository.findById(restaurantTable.getId());
		if(optional.isPresent()) {
			return restaurantTableRepository.save(restaurantTable);
		}
		return null;
	}
	
	
	// 查詢餐廳桌位byId
	public RestaurantTable findRestaurantTableById(RestaurantTableId restaurantTableId) {
		return restaurantTableRepository.findById(restaurantTableId).orElse(null);
	}

	
	// 查詢餐廳(id)所有桌位
	public List<RestaurantTable> findAllRestaurantTable(Integer restaurantId) {
		return restaurantTableRepository.findRestaurantTableByRestaurantId(restaurantId);
	}
	
	
	
	

	
	
	
}
