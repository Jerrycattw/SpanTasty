package com.eatspan.SpanTasty.controller.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;
import com.eatspan.SpanTasty.service.reservation.RestaurantTableService;
import com.eatspan.SpanTasty.service.reservation.TableTypeService;

@Controller
@RequestMapping("/table")
public class RestaurantTableController {
	
	
	@Autowired
	private RestaurantTableService restaurantTableService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private TableTypeService tableTypeService;
	
	
    @GetMapping("/getAll")
    public String getAllTables(Model model, @RequestParam Integer restaurantId) {
        List<RestaurantTable> tables = restaurantTableService.findAllRestaurantTable(restaurantId);
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        List<TableType> tableTypes = tableTypeService.findAllTableType();
        model.addAttribute("tables", tables);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("tableTypes", tableTypes);
        return "spantasty/reservation/getAllTable";
    }
	
	
	@PostMapping("/add")
	public String addTable(@RequestParam Integer restaurantId,
						   @RequestParam String tableTypeId,
						   @ModelAttribute RestaurantTable restaurantTable,
						   Model model) {
		restaurantTable.setId(new RestaurantTableId(restaurantId, tableTypeId));
		restaurantTableService.addRestaurantTable(restaurantTable);
		return "redirect:/table/getAll?restaurantId="+restaurantId;
	}
	
	
	
	@PutMapping("/set")
	public String updateTable(@RequestParam Integer restaurantId,
							  @RequestParam String tableTypeId,
							  @RequestParam Integer tableTypeNumber,
							  Model model) {
		
		RestaurantTable restaurantTable = restaurantTableService.findRestaurantTableById(new RestaurantTableId(restaurantId, tableTypeId));
		restaurantTable.setTableTypeNumber(tableTypeNumber);
		restaurantTableService.updateRestaurantTable(restaurantTable);
		return "redirect:/table/getAll?restaurantId="+restaurantId;
	}
	
	
	
	
	@DeleteMapping("/del")
	public ResponseEntity<?> delTable(@RequestParam Integer restaurantId, @RequestParam String tableTypeId) {
		restaurantTableService.deleteRestaurantTable(new RestaurantTableId(restaurantId, tableTypeId));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	
	
}
