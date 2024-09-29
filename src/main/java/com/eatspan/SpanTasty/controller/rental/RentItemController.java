package com.eatspan.SpanTasty.controller.rental;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;

@Controller
@RequestMapping("/rentItem/*")
public class RentItemController {
	@Autowired
	private RentItemService rentItemService;
	@Autowired
	private RentService rentService;
	
	@GetMapping("showAll")
	public String showAllRentItems(Model model) {
		List<RentItem> rentItems = rentItemService.findAllRentItems();
		model.addAttribute("rentItems",rentItems);
		return "rental/ShowAllRentItems";
	}
	
	@GetMapping("search")
	protected String showRentItemsBySearch(@RequestParam("rentId") Integer rentId, Model model) {
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
		model.addAttribute("rentItems", rentItems);
		return "rental/ShowAllRentItems";
	}
	
	@GetMapping("option")
	public String findSelectOption(Model model) {
		List<Integer> rentIds = rentService.findRentIds();
		model.addAttribute("rentIds",rentIds);
		return "rental/FindRentItemsBySearch";
	}
	
	@GetMapping("find")
	protected String findById(
			@RequestParam("rent_id") Integer rentId,
			@RequestParam("tableware_id") Integer tablewareId, 
			@RequestParam("action") String action, 
			Model model) {
		RentItem rentItem = rentItemService.findRentItemByRentItemId(rentId, tablewareId);
		model.addAttribute("rentItem", rentItem);
		if ("update".equals(action)) {
			return "rental/UpdateRentItem";
		} else if ("return".equals(action)) {
			return "rental/ReturnRentItem";
		}
		return null;
	}
	
	@PutMapping("update")
	protected String update(
			@RequestParam("rent_id") Integer rentId, 
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("rent_item_quantity") Integer rentItemQuantity,
			@RequestParam("rent_item_deposit") Integer rentItemDeposit, 
			@RequestParam("return_memo") String returnMemo,
			@RequestParam("return_status") Integer returnStatus,
			Model model) {
		RentItem rentItem = rentItemService.findRentItemByRentItemId(rentId, tablewareId);
		rentItem.setRentId(rentId);
		rentItem.setTablewareId(tablewareId);
		rentItem.setRentItemQuantity(rentItemQuantity);
		rentItem.setRentItemDeposit(rentItemDeposit);
		rentItem.setReturnMemo(returnMemo);
		rentItem.setReturnStatus(returnStatus);
		rentItemService.saveRentItem(rentItem);
		return "redirect:/rentItem/showAll";
	}
	
	@GetMapping("return")
	protected String returnRentItem(
			@RequestParam("rent_id") Integer rentId, 
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("return_memo") String returnMemo,
			@RequestParam("return_status") Integer returnStatus,
			Model model) {
		RentItem rentItem = rentItemService.findRentItemByRentItemId(rentId, tablewareId);
		rentItem.setReturnMemo(returnMemo);
		rentItem.setReturnStatus(returnStatus);
		rentItemService.saveRentItem(rentItem);
		return "redirect:/rentItem/showAll";
	}
}
