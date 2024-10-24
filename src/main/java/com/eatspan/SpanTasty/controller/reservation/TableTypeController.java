package com.eatspan.SpanTasty.controller.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.service.reservation.TableTypeService;

@Controller
@RequestMapping("/tabletype")
public class TableTypeController {
	
	@Autowired
	private TableTypeService tableTypeService;
	
	
	@GetMapping("/getAll")
	public String getAllTableType(Model model) {
		List<TableType> tableTypes = tableTypeService.findAllTableType();
		model.addAttribute("tableTypes", tableTypes);
		return "spantasty/reservation/getAllTableType";
	}
	
	
	@PostMapping("/add")
	public String addTableType(@ModelAttribute TableType tableType) {
		tableTypeService.addTableType(tableType);
		return "redirect:/tabletype/getAll";
	}
	
	
	@DeleteMapping("/del")
	public ResponseEntity<?> delTableType(@RequestParam String tableTypeId) {
		tableTypeService.deleteTableType(tableTypeId);
		return ResponseEntity.ok("Delete Success");
	}
	
	
	
	
	
}
