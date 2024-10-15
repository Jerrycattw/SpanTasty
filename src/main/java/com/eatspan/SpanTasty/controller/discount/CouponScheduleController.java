package com.eatspan.SpanTasty.controller.discount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.discount.CouponScheduleDTO;
import com.eatspan.SpanTasty.entity.discount.CouponSchedule;
import com.eatspan.SpanTasty.service.discount.CouponScheduleService;

@Controller
public class CouponScheduleController {
	
	@Autowired
	private CouponScheduleService couponScheduleService;
	
	
	@GetMapping("/coupon/schedule")
	public String schedule() {
		return "spantasty/discount/coupon/couponSchedule";
	}
	
	@GetMapping("/coupon/schedule/show")
	@ResponseBody
	public List<CouponScheduleDTO> getAllCouponSchedule() {
		return couponScheduleService.findAllCouponSchedules();
	}
}
