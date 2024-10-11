package com.eatspan.SpanTasty.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StarCupsPageController {
	
	@GetMapping("/StarCups/test")
	public String test() {
		return "starcups/account/NewFile"; 
	}

}
