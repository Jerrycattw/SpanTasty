package com.eatspan.SpanTasty.controller.discount;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.service.discount.CouponMemberService;


@Controller
@RequestMapping("/StarCups")
public class StarCupsCouponController {
	
	@Autowired
	private CouponMemberService couponMemberService;
	
	
	@GetMapping("/coupon")
	public String showPoint() {
		return "/starcups/discount/coupon";
	}
	
	@GetMapping("/coupon/show")
	@ResponseBody
	public Map<String, List<CouponMember>> getMethodName(@RequestParam Integer memberId) {
		return couponMemberService.starCupsCoupon(memberId);
	}
	
}
