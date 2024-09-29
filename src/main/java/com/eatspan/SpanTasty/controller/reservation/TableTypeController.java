package com.eatspan.SpanTasty.controller.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatspan.SpanTasty.service.reservation.TableTypeService;

@Controller
@RequestMapping("/tabletype")
public class TableTypeController {
	
	@Autowired
	private TableTypeService tableTypeService;
	
	
}
