package com.eatspan.SpanTasty.controller.rental;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatspan.SpanTasty.dto.rental.RestaurantStockDTO;
import com.eatspan.SpanTasty.dto.rental.TablewareFilterDTO;
import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/StarCups")
public class StarCupsRentController {

	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private TablewareStockService tablewareStockService;
	@Autowired
	private RentService rentService;
	@Autowired
	private RentItemService rentItemService;
	@Autowired
	private RestaurantService restaurantService;
	
	
	// 導向到LOOKBOOK頁面
	@GetMapping("/tableware")
	public String showAllTablewares(Model model, @RequestParam(defaultValue = "1") Integer page) {
		Page<Tableware> tablewarePages = tablewareService.findAllTablewarePages(page);
		Integer totalTablewares = tablewareService.countTableware();
		model.addAttribute("totalTablewares", totalTablewares);
		model.addAttribute("tablewarePages", tablewarePages);
		return "starcups/rental/allTablewarePage";
	}
	
	
	// 導向到RENT頁面
	@GetMapping("/rent")
	public String toRentTableware(Model model) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants()
				.stream()
                .filter(restaurant -> restaurant.getRestaurantStatus() == 1)
                .collect(Collectors.toList());
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		model.addAttribute("tablewares", tablewares);
		model.addAttribute("restaurants" ,restaurants);
		return "starcups/rental/rentTablewarePage";
	}
	
	
	// 選擇餐廳後跑出的資料
	@ResponseBody
	@PostMapping("/rent/get")
	public ResponseEntity<RestaurantStockDTO> postMethodName(@RequestBody TablewareFilterDTO tablewareFilterDTO) {
		Restaurant restaurant = restaurantService.findRestaurantById(tablewareFilterDTO.getRestaurantId());
		List<TablewareStock> stocks = tablewareStockService.findStockByRestaurantId(tablewareFilterDTO.getRestaurantId());
		
		RestaurantStockDTO restaurantStockDTO = new RestaurantStockDTO();
		restaurantStockDTO.setRestaurant(restaurant);
		restaurantStockDTO.setStocks(stocks);
		return ResponseEntity.ok(restaurantStockDTO);
	}
	

}
