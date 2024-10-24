package com.eatspan.SpanTasty.service.reservation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.dto.reservation.RestaurantDTO;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.repository.reservation.RestaurantRepository;

@Service
@PropertySource("upload.properties")
public class RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Value("${upload.reservationPath}")
	private String uploadPath;
	
	
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
	
	
	// 上傳照片至upload資料夾
	public Restaurant uploadFile(Restaurant restaurant, MultipartFile file) throws IllegalStateException, IOException {
		
        // 建立圖片保存的目錄
        File fileSaveDirectory = new File(uploadPath);
        if (!fileSaveDirectory.exists()) {
            fileSaveDirectory.mkdirs();
        }

        // 檢查文件是否不為空，並處理上傳
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = restaurant.getRestaurantName() + extension;

            // 保存檔案到指定路徑
            File fileToSave = new File(uploadPath + File.separator + newFileName);
            file.transferTo(fileToSave);
            restaurant.setRestaurantImg("/SpanTasty/upload/reservation/" + newFileName);
        } else {
        	// 如果是修改餐廳，保留原有圖片
        	if (restaurant.getRestaurantId() != null) {
        		String restaurantImg = findRestaurantById(restaurant.getRestaurantId()).getRestaurantImg();
        		restaurant.setRestaurantImg(restaurantImg);
        	}
		}
        
        return restaurant;
		
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
