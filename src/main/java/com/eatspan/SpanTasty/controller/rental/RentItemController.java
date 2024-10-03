package com.eatspan.SpanTasty.controller.rental;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;

@Controller
@RequestMapping("/rentItem")
public class RentItemController {
	
	@Autowired
	private RentItemService rentItemService;
	@Autowired
	private RentService rentService;
	
	
	//導向查詢頁面
	@GetMapping("/toGet")
	public String getRentIdOption(Model model) {
		List<Rent> rents = rentService.findAllRents();
		model.addAttribute("rents",rents);
		return "rental/getRentItemsBySearch";
	}
	
	
	//查詢訂單明細
	@GetMapping("/get")
	protected String getRentItemsBySearch(@RequestParam("rentId") Integer rentId, Model model) {
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
		model.addAttribute("rentItems", rentItems);
		return "rental/getAllRentItems";
	}

	
	//導向update的頁面
	@GetMapping("/set")
	protected String toSetRentItem(
			@RequestParam("rent_id") Integer rentId,
			@RequestParam("tableware_id") Integer tablewareId, 
			@RequestParam("action") String action, 
			Model model) {
		RentItem rentItem = rentItemService.findRentItemById(rentId, tablewareId);
		model.addAttribute("rentItem", rentItem);
		if ("update".equals(action)) {
			return "rental/updateRentItem";
		} else if ("return".equals(action)) {
			return "rental/returnRentItem";
		}
		return null;
	}
	
	
	//網頁寫在Rent的某個頁面(1.訂單頁面可查詢單筆訂單的所有明細,點進去可修改(批次更新?))
	@PutMapping("/setPut1")
	protected String updateRentItem(
			@RequestParam("rent_id") Integer rentId, 
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("rent_item_quantity") Integer rentItemQuantity,
			@RequestParam("rent_item_deposit") Integer rentItemDeposit, 
			@RequestParam("return_memo") String returnMemo,
			@RequestParam("return_status") Integer returnStatus,
			Model model) {
		RentItem rentItem = rentItemService.findRentItemById(rentId, tablewareId);
		rentItem.setRentId(rentId);
		rentItem.setTablewareId(tablewareId);
		rentItem.setRentItemQuantity(rentItemQuantity);
		rentItem.setRentItemDeposit(rentItemDeposit);
		rentItem.setReturnMemo(returnMemo);
		rentItem.setReturnStatus(returnStatus);
		rentItemService.addRentItem(rentItem);
		return "redirect:/rentItem/showAll";
	}
	
	
	//網頁寫在Rent的歸還頁面(歸還可選擇歸還狀態 根據情況再加庫存)
	@PutMapping("/setPut2")
	protected String returnRentItem(
			@RequestParam("rent_id") Integer rentId, 
			@RequestParam("tableware_id") Integer tablewareId,
			@RequestParam("return_memo") String returnMemo,
			@RequestParam("return_status") Integer returnStatus,
			Model model) {
		RentItem rentItem = rentItemService.findRentItemById(rentId, tablewareId);
		rentItem.setReturnMemo(returnMemo);
		rentItem.setReturnStatus(returnStatus);
		rentItemService.addRentItem(rentItem);
		return "redirect:/rentItem/showAll";
	}
	
	
//	查詢所有訂單明細
//	@GetMapping("/getAll")
//	public String getAllRentItems(Model model) {
//		List<RentItem> rentItems = rentItemService.findAllRentItems();
//		model.addAttribute("rentItems",rentItems);
//		return "rental/ShowAllRentItems";
//	}
}
