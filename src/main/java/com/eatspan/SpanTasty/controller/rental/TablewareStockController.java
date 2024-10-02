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
	
	@GetMapping("/getAll")
	public String showAllTablewareStocks(Model model) {
		List<TablewareStock> stocks = tablewareStockService.findAllStocks();
		model.addAttribute("stocks",stocks);
		return "rental/showAllStocks";
	}
	
	@GetMapping("/selectTR")
	public String findSelectOption(@RequestParam("action") String action, Model model) {
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		model.addAttribute("tablewares",tablewares);
		model.addAttribute("restaurants",restaurants);
		if ("insert".equals(action)) {
			return "rental/addStock";
	    } else if ("search".equals(action)) {
	    	return "rental/searchStocks";
	    }
		return null;
	}
	
	@PostMapping("/add")
	protected String addStock(@ModelAttribute TablewareStock stock, Model model) {
		tablewareStockService.addStock(stock);
		return "redirect:/tablewareStock/showAll";
	}

	@PutMapping("/set")
	protected String update(
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("restaurant_id") Integer restaurantId,
			@RequestParam("stock") Integer stock,
			Model model) {
		TablewareStock tablewareStock = tablewareStockService.findStockById(tablewareId, restaurantId);
		tablewareStock.setStock(stock);
		tablewareStockService.addStock(tablewareStock);
		model.addAttribute("stock", tablewareStock);
		return "redirect:/tablewareStock/showAll";
	}

	@GetMapping("/search")
	protected String search(
			@RequestParam(value = "tablewareId", required = false) Integer tablewareId,
			@RequestParam(value = "restaurantName", required = false) String restaurantName,
			Model model) {
//		Integer restaurantId = restaurantService.getRestaurantId(restaurantName);
		Integer restaurantId = null;
		List<TablewareStock> tablewareStocks = tablewareStockService.findStocksByCriteria(tablewareId, restaurantId);
		model.addAttribute("stock", tablewareStocks);
		return "rental/ShowAllTablewareStocks";
	}
	
	@GetMapping("/get")
	protected String findTablewareStockByStockId(
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("restaurant_id") Integer restaurantId,
			Model model) {
		TablewareStock tablewareStock = tablewareStockService.findStockById(tablewareId, restaurantId);
		model.addAttribute("stock", tablewareStock);
		return "rental/UpdateStock";
	}
}
