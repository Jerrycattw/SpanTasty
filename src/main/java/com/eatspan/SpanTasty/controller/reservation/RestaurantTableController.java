package com.eatspan.SpanTasty.controller.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatspan.SpanTasty.service.reservation.RestaurantTableService;

@Controller
@RequestMapping("/table")
public class RestaurantTableController {
	
	
	@Autowired
	private RestaurantTableService restaurantTableService;
	
	
}
