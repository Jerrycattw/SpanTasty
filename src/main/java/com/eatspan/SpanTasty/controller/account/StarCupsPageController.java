package com.eatspan.SpanTasty.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StarCupsPageController {
	
	@GetMapping("/StarCups/test")
	public String test() {
		return "starcups/account/memberCenter2"; 
	}
	
	@GetMapping("/StarCups/loginRegister")
	public String showLoginRegisterPage() {
		return "starcups/account/login-register"; 
	}
	@GetMapping("/StarCups/memberCenter")
	public String showMemberCenterPage() {
		return "starcups/account/memberCenter"; 
	}

}
