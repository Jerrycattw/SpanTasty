package com.eatspan.SpanTasty.controller.rental;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.rental.TablewareStockService;

@Controller
@RequestMapping("/tablewareStock/*")
public class TablewareStockController {
	@Autowired
	private TablewareStockService tablewareStockService;
	@Autowired
	private TablewareService tablewareService;
	
	@GetMapping("showAll")
	public String showAllTablewareStocks(Model model) {
		List<TablewareStock> tablewareStocks = tablewareStockService.findAllTablewareStocks();
		model.addAttribute("tablewareStocks",tablewareStocks);
		return "rental/ShowAllTablewareStocks";
	}
	
	@GetMapping("option")
	public String findSelectOption(@RequestParam("action") String action, Model model) {
		List<Integer> tablewareIds = tablewareService.findTablewareIds();
//		List<String> restaurantNames = restaurantService.getAllRestaurantName();
		model.addAttribute("tablewareIds",tablewareIds);
//		model.addAttribute("restaurantNames",restaurantNames);
		if ("insert".equals(action)) {
			return "rental/AddTablewareStock";
	    } else if ("search".equals(action)) {
	    	return "rental/SearchTablewareStock";
	    }
		return null;
	}
	
	@PostMapping("insert")
	protected String addStock(
			@RequestParam("tablewareId") Integer tablewareId,
			@RequestParam("restaurantName") String restaurantName,
			@RequestParam("stock") Integer stock,
			Model model) {
//		Integer restaurantId = restaurantService.getRestaurantId(restaurantName);
		Integer restaurantId = null;
		TablewareStock tablewareStock = new TablewareStock();
		tablewareStock.setTablewareId(tablewareId);
		tablewareStock.setRestaurantId(restaurantId);
		tablewareStock.setStock(stock);
		tablewareStockService.saveTablewareStock(tablewareStock);
		model.addAttribute("tableware_stock", tablewareStock);
		return "redirect:/tablewareStock/showAll";
	}

	@PutMapping("update")
	protected String update(
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("restaurant_id") Integer restaurantId,
			@RequestParam("stock") Integer stock,
			Model model) {
		TablewareStock tablewareStock = tablewareStockService.findTablewareStockByStockId(tablewareId, restaurantId);
		tablewareStock.setStock(stock);
		tablewareStockService.saveTablewareStock(tablewareStock);
		model.addAttribute("stock", tablewareStock);
		return "redirect:/tablewareStock/showAll";
	}

	@GetMapping("search")
	protected String search(
			@RequestParam(value = "tablewareId", required = false) Integer tablewareId,
			@RequestParam(value = "restaurantName", required = false) String restaurantName,
			Model model) {
//		Integer restaurantId = restaurantService.getRestaurantId(restaurantName);
		Integer restaurantId = null;
		List<TablewareStock> tablewareStocks = tablewareStockService.findTablewareStocksByCriteria(tablewareId, restaurantId);
		model.addAttribute("stock", tablewareStocks);
		return "rental/ShowAllTablewareStocks";
	}
	
	@GetMapping("getById")
	protected String findTablewareStockByStockId(
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("restaurant_id") Integer restaurantId,
			Model model) {
		TablewareStock tablewareStock = tablewareStockService.findTablewareStockByStockId(tablewareId, restaurantId);
		model.addAttribute("stock", tablewareStock);
		return "rental/UpdateStock";
	}
}
