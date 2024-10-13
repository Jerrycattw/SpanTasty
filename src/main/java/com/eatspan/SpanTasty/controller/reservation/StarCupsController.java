package com.eatspan.SpanTasty.controller.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.reservation.ReserveCheckDTO;
import com.eatspan.SpanTasty.dto.reservation.ReserveDTO;
import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.reservation.ReserveService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;
import com.eatspan.SpanTasty.service.reservation.RestaurantTableService;
import com.eatspan.SpanTasty.service.reservation.TableTypeService;

@Controller
@RequestMapping("/StarCups")
public class StarCupsController {
	
	
	@Autowired
	private ReserveService reserveService;
	@Autowired
	private RestaurantTableService restaurantTableService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private TableTypeService tableTypeService;
	@Autowired
	private MemberService memberService;
	
	
	
	// 導向到訂位頁面
    @GetMapping("/reserve")
    public String showReserve(Model model) {
        List<TableType> tableTypes = tableTypeService.findAllTableType();
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        model.addAttribute("tableTypes", tableTypes);
        model.addAttribute("restaurants", restaurants);
        return "starcups/reservation/reservePage";
    }
    
    // 導向到所有餐廳頁面
    @GetMapping("/restaurant")
    public String showAllRestaurant(Model model) {
//    	List<TableType> tableTypes = tableTypeService.findAllTableType();
    	List<Restaurant> restaurants = restaurantService.findAllRestaurants();
//    	model.addAttribute("tableTypes", tableTypes);
    	model.addAttribute("restaurants", restaurants);
    	return "starcups/reservation/allRestaurantPage";
    }
	
    
    
    
	
	
}
