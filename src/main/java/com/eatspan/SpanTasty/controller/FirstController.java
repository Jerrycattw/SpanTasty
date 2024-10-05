package com.eatspan.SpanTasty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FirstController {

	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	

	
}
