package com.eatspan.SpanTasty.controller.rental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/StarCups")
public class StarCupsRentController {

	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private TablewareStockService tablewareStockService;
	@Autowired
	private RentService rentService;
	@Autowired
	private RentItemService rentItemService;
	
	
	// 導向到租借餐具頁面
	@GetMapping("/tableware")
	public String showAllTablewares(Model model, @RequestParam(defaultValue = "1") Integer page) {
		Page<Tableware> tablewaresPage = tablewareService.findAllTablewarePages(page);
		model.addAttribute("tablewaresPage", tablewaresPage);
		return "starcups/rental/allTablewarePage";
	}
	
	

}
