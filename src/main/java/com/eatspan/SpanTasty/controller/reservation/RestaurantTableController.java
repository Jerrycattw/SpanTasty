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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.dto.reservation.RestaurantTableDTO;
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
						   @RequestParam Integer tableNumber,
						   Model model) {
		
		for(int i=0 ; i<tableNumber ; i++) {
			restaurantTableService.addRestaurantTable(restaurantId, tableTypeId);
		}
		return "redirect:/table/getAll?restaurantId="+restaurantId;
	}
	
	
	
	@PutMapping("/set")
	public ResponseEntity<?> updateTable(@RequestBody RestaurantTableDTO restaurantTableDTO) {
		RestaurantTableId restaurantTableId = new RestaurantTableId(restaurantTableDTO.getRestaurantId(),restaurantTableDTO.getTableTypeId(),restaurantTableDTO.getTableId());
		RestaurantTable restaurantTable = restaurantTableService.findRestaurantTableById(restaurantTableId);
		restaurantTable.setTableStatus(restaurantTableDTO.getTableStatus());
		RestaurantTable newRestaurantTable = restaurantTableService.updateRestaurantTable(restaurantTable);
		return ResponseEntity.ok(newRestaurantTable);
	}
	
	
	
	
	@DeleteMapping("/del")
	public ResponseEntity<?> delTable(@RequestParam Integer restaurantId, @RequestParam String tableTypeId, @RequestParam Integer tableId) {
		restaurantTableService.deleteRestaurantTable(new RestaurantTableId(restaurantId, tableTypeId, tableId));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	
	
}
