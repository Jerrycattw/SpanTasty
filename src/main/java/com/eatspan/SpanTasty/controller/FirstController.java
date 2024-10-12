package com.eatspan.SpanTasty.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class FirstController {

	@GetMapping("/")
	public String home() {
		return "spantasty/index";
	}
	
	
	
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	
	
	@GetMapping("/StarCups")
	public String starCups() {
		return "starcups/index";
	}
	
	
}
