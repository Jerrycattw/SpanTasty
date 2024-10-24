package com.eatspan.SpanTasty.service.reservation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.dto.reservation.RestaurantDTO;
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
	
	
	
	// 根據地址欄位解析縣市
    public List<String> getAllCities() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        Set<String> cities = new HashSet<>();
        for (Restaurant restaurant : restaurants) {
            String city = city(restaurant.getRestaurantAddress());
            if (city != null && !city.isEmpty()) {
                cities.add(city);
            }
        }
        return new ArrayList<>(cities);
    }

    // 根據縣市解析鄉鎮
    public List<String> getTownsByCity(String city) {
        List<Restaurant> restaurants = restaurantRepository.findByRestaurantAddressContaining(city);
        Set<String> towns = new HashSet<>();
        for (Restaurant restaurant : restaurants) {
            String town = town(restaurant.getRestaurantAddress(), city);
            if (town != null && !town.isEmpty()) {
                towns.add(town);
            }
        }
        return new ArrayList<>(towns);
    }
    
    public List<RestaurantDTO> getRestaurantsByTown(String town) {
        List<Restaurant> restaurants = restaurantRepository.findByRestaurantAddressContaining(town);
        List<RestaurantDTO> restaruantDtos = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
        	RestaurantDTO restaurantDto = new RestaurantDTO();
        	restaurantDto.setRestaurantId(restaurant.getRestaurantId());
        	restaurantDto.setRestaurantName(restaurant.getRestaurantName());
        	restaurantDto.setRestaurantAddress(restaurant.getRestaurantAddress());
        	restaruantDtos.add(restaurantDto);
        }
        return restaruantDtos;
    }
    
    // 縣市
    private String city(String address) {
    	if (address != null && !address.isEmpty()) {
            String city = null;
            if (address.contains("市")) {
                // 市
                String[] parts = address.split("市");
                if (parts.length > 0) {
                    city = parts[0] + "市";
                }
            } else if (address.contains("縣")) {
                // 縣
                String[] parts = address.split("縣");
                if (parts.length > 0) {
                    city = parts[0] + "縣";
                }
            }
            return city;
        }
        return null;
    }

    // 鄉鎮
    private String town(String address, String city) {
    	if (address != null && !address.isEmpty() && city != null) {
            // 去除縣市部分
            String remainingAddress = address.replace(city, "");
            
            // 判斷鄉鎮市區
            if (remainingAddress.contains("區")) {
                return remainingAddress.split("區")[0] + "區";
            } else if (remainingAddress.contains("鄉")) {
                return remainingAddress.split("鄉")[0] + "鄉";
            } else if (remainingAddress.contains("鎮")) {
                return remainingAddress.split("鎮")[0] + "鎮";
            } else if (remainingAddress.contains("市")) {
                return remainingAddress.split("市")[0] + "市";
            }
        }
        return null;
    }

}
