package com.eatspan.SpanTasty.controller.discount;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StarCupsCouponController {

	@GetMapping("/StarCups/coupon")
	public String showPoint() {
		return "/starcups/discount/coupon";
	}
}
