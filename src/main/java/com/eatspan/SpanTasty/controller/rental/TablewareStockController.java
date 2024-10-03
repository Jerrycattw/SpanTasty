package com.eatspan.SpanTasty.controller.rental;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

@Controller
@RequestMapping("/stock")
public class TablewareStockController {
	
	@Autowired
	private TablewareStockService tablewareStockService;
	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private RestaurantService restaurantService;
	
	
	// 導向頁面(新增 搜尋)
	@GetMapping("/add")
	public String toAddAndSearch(@RequestParam("action") String action, Model model) {
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		model.addAttribute("tablewares",tablewares);
		model.addAttribute("restaurants",restaurants);
		if ("insert".equals(action)) {
			return "rental/addStock";
	    } else if ("search".equals(action)) {
	    	return "rental/getStocks";
	    }
		return null;
	}
	
	
	// 新增庫存
	@PostMapping("/addstock")
	protected String addStock(@ModelAttribute TablewareStock stock, Model model) {
		tablewareStockService.addStock(stock);
		return "redirect:/stock/getAll";
	}

	
	// 導向更新頁面
	@GetMapping("/set")
	protected String toSetStock(
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("restaurant_id") Integer restaurantId,
			Model model) {
		TablewareStock stock = tablewareStockService.findStockById(tablewareId, restaurantId);
		model.addAttribute("stock", stock);
		return "rental/updateStock";
	}
	
	
	// 更新庫存
	@PutMapping("/setPut")
	protected String update(
			@ModelAttribute TablewareStock stock,
			Model model) {
		tablewareStockService.addStock(stock);
		return "redirect:/stock/getAll";
	}

	
	// 查詢庫存
	@GetMapping("/get")
	protected String getStocks(
			@RequestParam(value = "tablewareId", required = false) Integer tablewareId,
			@RequestParam(value = "restaurantId", required = false) Integer restaurantId,
			Model model) {
		List<TablewareStock> stocks = tablewareStockService.findStocksByCriteria(tablewareId, restaurantId);
		model.addAttribute("stocks", stocks);
		return "rental/getAllStocks";
	}
	
	
	// 查詢所有庫存
	@GetMapping("/getAll")
	public String getAllStocks(Model model) {
		List<TablewareStock> stocks = tablewareStockService.findAllStocks();
		model.addAttribute("stocks",stocks);
		return "rental/getAllStocks";
	}
}
