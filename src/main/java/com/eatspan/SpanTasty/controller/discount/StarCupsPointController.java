package com.eatspan.SpanTasty.controller.discount;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StarCupsPointController {
	
	@GetMapping("/StarCups/point")
	public String showPoint() {
		return "/starcups/discount/point";
	}
}
