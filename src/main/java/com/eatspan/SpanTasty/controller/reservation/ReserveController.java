package com.eatspan.SpanTasty.controller.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.reservation.ReserveCheckDTO;
import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.service.reservation.ReserveService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;
import com.eatspan.SpanTasty.service.reservation.RestaurantTableService;
import com.eatspan.SpanTasty.service.reservation.TableTypeService;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	
	@Autowired
	private ReserveService reserveService;
	@Autowired
	private RestaurantTableService restaurantTableService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private TableTypeService tableTypeService;
	
	
	
	// 導向到查詢訂單頁面
    @GetMapping("/getAll")
    public String showReserve(Model model) {
        List<TableType> tableTypes = tableTypeService.findAllTableType();
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        model.addAttribute("tableTypes", tableTypes);
        model.addAttribute("restaurants", restaurants);
        return "reservation/getAllReserve";
    }
	
    
    
    // ajax 查詢訂位訂單
    @GetMapping("/getList")
    @ResponseBody
    public List<Reserve> getListReserve(@RequestParam(required = false) String memberName,
							            @RequestParam(required = false) String phone,
							            @RequestParam(required = false) Integer restaurantId,
							            @RequestParam(required = false) String tableTypeId,
							            @RequestParam(required = false) LocalDateTime reserveTimeStart,
							            @RequestParam(required = false) LocalDateTime reserveTimeEnd) {
    	
    	List<Reserve> reserveByCriteria = reserveService.findReserveByCriteria(memberName, phone, restaurantId, tableTypeId, reserveTimeStart, reserveTimeEnd);
    	return reserveByCriteria;
    }
    
    
    
    // ajax查詢可訂位時段
    @GetMapping("/getReserveCheck")
    @ResponseBody
    public List<ReserveCheckDTO> getReserveCheck(@RequestParam Integer restaurantId,
									    		 @RequestParam Integer reserveSeat,
									    		 @RequestParam LocalDate checkDate) {
    	
    	String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserveSeat);
    	List<ReserveCheckDTO> reserveCheck = reserveService.getReserveCheck(restaurantId, tableTypeId, checkDate);
    	return reserveCheck;
    }
    
    
    
    
	@DeleteMapping("/del")
	@ResponseBody
	public String delTable(@RequestParam Integer reserveId) {
		reserveService.deleteReserve(reserveId);
		return "delete ok";
	}
    
    
	
	
	// 新增訂位的ajax
	@PostMapping("/add")
	@ResponseBody
	public String addReserve(@RequestBody Reserve reserve) {
		return null;
	}
	
	
	// 修改訂位的ajax
	@PutMapping("/set")
	@ResponseBody
	public String updateReserve() {
		
		return null;
	}
    
    
	
	
}
