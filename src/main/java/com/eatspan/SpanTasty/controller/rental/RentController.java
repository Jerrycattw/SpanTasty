package com.eatspan.SpanTasty.controller.rental;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.rental.RentItem.RentItemId;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;
import com.eatspan.SpanTasty.service.rental.TablewareService;

@Controller
@RequestMapping("/rent/*")
public class RentController {
	@Autowired
	private RentService rentService;
	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private RentItemService rentItemService;

	@GetMapping("showAll")
	public String showAllRents(Model model) {
		List<Rent> rents = rentService.findAllRents();
		model.addAttribute("rents",rents);
		return "rental/ShowAllRents";
	}
	
	@GetMapping("option1")
	public String findSelectOption1(Model model) {
		List<Integer> tablewareIds = tablewareService.findTablewareIds();
		model.addAttribute("tablewareIds" ,tablewareIds);
		return "rental/SaveRent";
	}
	
	@GetMapping("option2")
	public String findSelectOption2(Model model) {
		List<Integer> rentIds = rentService.findRentIds();
		model.addAttribute("rentIds", rentIds);
		return "rental/findRentsBySearch";
	}
	
	@PostMapping("save")
	public String addRentAndItems(
			@RequestParam("rent_deposit") Integer rentDeposit,
//			@RequestParam("restaurantName") String restaurantName, 
//			@RequestParam("memberName") String memberName,
			@RequestParam("member_id") Integer memberId,
			@RequestParam Map<String, String> allParams,
			Model model) {
		Date rentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rentDate);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		Date dueDate = calendar.getTime();
		Integer restaurantId = null;
		Rent rent = new Rent();
		rent.setRentDeposit(rentDeposit);
		rent.setRentDate(rentDate);
		rent.setRestaurantId(restaurantId);
		rent.setMemberId(memberId);
		rent.setDueDate(dueDate);
		rent.setRentStatus(1);
		rent.setRentMemo("未歸還");
		rentService.saveRent(rent);
		
		int rentId = rent.getRentId();
		
		List<String> tablewareIds = new ArrayList<>();
	    List<String> rentItemQuantities = new ArrayList<>();
	    List<String> rentItemDeposits = new ArrayList<>();

	    for (Map.Entry<String, String> entry : allParams.entrySet()) {
	        if (entry.getKey().startsWith("tablewareId")) {
	            tablewareIds.add(entry.getValue());
	        } else if (entry.getKey().startsWith("rentItemQuantity")) {
	            rentItemQuantities.add(entry.getValue());
	        } else if (entry.getKey().startsWith("rentItemDeposit")) {
	            rentItemDeposits.add(entry.getValue());
	        }
	    }
		
		List<RentItem> rentItems = new ArrayList<>();
		for (int i = 0; i < tablewareIds.size(); i++) {
	        Integer tablewareId = Integer.parseInt(tablewareIds.get(i));
	        Integer rentItemQuantity = Integer.parseInt(rentItemQuantities.get(i));
	        Integer rentItemDeposit = Integer.parseInt(rentItemDeposits.get(i));

	        RentItem rentItem = new RentItem();
	        rentItem.setRentId(rentId);
	        rentItem.setTablewareId(tablewareId);
	        rentItem.setRentItemQuantity(rentItemQuantity);
	        rentItem.setRentItemDeposit(rentItemDeposit);
	        rentItem.setReturnMemo("未歸還");
	        rentItem.setReturnStatus(1);
	        rentItems.add(rentItem);
	    }
		for (RentItem rentItem : rentItems) {
	        rentItemService.saveRentItem(rentItem);
	    }
		
		return "redirect:/rent/showAll";
	}
	
	@GetMapping("find")
	public String getById(@RequestParam("rent_id") Integer rentId, @RequestParam("action") String action, Model model) {
		Rent rent = rentService.findRentById(rentId);
		model.addAttribute("rent", rent);
		if ("update".equals(action)) {
			return "rental/UpdateRent";
		} else if ("restore".equals(action)) {
			return "rental/ReturnRent";
		}
		return null;
	}
	
	@DeleteMapping("delete")
	public String deleteMsgById(@RequestParam("rent_id") Integer rentId, RentItemId rentItemId, Model model) {
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
		for(RentItem rentItem: rentItems) {
			rentItemService.deleteRentItem(rentItem);
		}
		rentService.deleteRentById(rentId);
		return "redirect:/rent/showAll";
	}
	
	@GetMapping("search")
	public String findRentsBySearch(
			@RequestParam(value = "rent_id" , required = false) Integer rentId,
			@RequestParam(value = "member_id" , required = false) Integer memberId,
//			@RequestParam(value = "restaurantName" , required = false) String restaurantName, 
			@RequestParam(value = "rent_status", required = false) Integer rentStatus,
			@RequestParam(value = "rent_date_start", required = false) String rentDateStartStr,
			@RequestParam(value = "rent_date_end", required = false) String rentDateEndStr,
			Model model) {
		try {
			rentId = (rentId != null) ? rentId : null;
			memberId = (memberId != null) ? memberId : null;
//			Integer restaurantId = restaurantService.getRestaurantId(restaurantName);
//	        restaurantId = (restaurantId != null && !restaurantId.isEmpty()) ? restaurantId : null;
			Integer restaurantId = null;
	        rentStatus = (rentStatus != null) ? rentStatus : null;
	        Date rentDateStart = (rentDateStartStr != null && !rentDateStartStr.isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(rentDateStartStr) : null;
	        Date rentDateEnd = (rentDateEndStr != null && !rentDateEndStr.isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(rentDateEndStr) : null;
	        
			List<Rent> rents = rentService.findRentsByCriteria(rentId, memberId, restaurantId, rentStatus, rentDateStart, rentDateEnd);
			model.addAttribute("rents", rents);
			return "rental/ShowAllRents";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("overtime")
	public String findOvertimeRents(Model model) {
		List<Rent> rents = rentService.findRentsByOvertime();
		model.addAttribute("rents", rents);
		return "rental/ShowAllRents";
	}
	
	@PutMapping("return")
	public String returnRent(
			@RequestParam("rent_id") Integer rentId,
//			@RequestParam("restaurantName") String restaurantName, 
			Model model) {
		try {
			Date returnDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(returnDate);
//			Integer returnRestaurantId = restaurantService.getRestaurantId(restaurantName);
			Integer returnRestaurantId = null;
			Rent rent = rentService.findRentById(rentId);
			rent.setReturnDate(returnDate);
			rent.setReturnRestaurantId(returnRestaurantId);
			rentService.saveRent(rent);
			return "redirect:/rent/showAll";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PutMapping("update")
	protected String updateRent(
			@RequestParam("rent_id") Integer rentId,
			@RequestParam("rent_deposit") Integer rentDeposit,
			@RequestParam("rent_date") String rentDateUtil,
//			@RequestParam("restaurantName") String restaurantName, 
			@RequestParam("member_id") Integer memberId,
			@RequestParam("due_date") String dueDateUtil,
			@RequestParam("return_date") String returnDateUtil,
			@RequestParam("rent_status") Integer rentStatus,
			@RequestParam("rent_memo") String rentMemo,
//			@RequestParam("returnRestaurantName") String returnRestaurantName, 
			Model model) {
		try {
			Date rentDate = new SimpleDateFormat("yyyy-MM-dd").parse(rentDateUtil);
//			Integer restaurantId = restaurantService.getRestaurantId(restaurantName);
			Integer restaurantId = null;
			Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateUtil);
			Date returnDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnDateUtil);
//			Integer returnRestaurantId = restaurantService.getRestaurantId(returnRestaurantName);
			Integer returnRestaurantId = null;
			
			Rent rent = new Rent();
			rent.setRentId(rentId);
			rent.setRentDeposit(rentDeposit);
			rent.setRentDate(rentDate);
			rent.setRestaurantId(restaurantId);
			rent.setMemberId(memberId);
			rent.setDueDate(dueDate);
			rent.setReturnDate(returnDate);
			rent.setRentStatus(rentStatus);
			rent.setRentMemo(rentMemo);
			rent.setReturnRestaurantId(returnRestaurantId);
			rentService.saveRent(rent);
			return "redirect:/rent/showAll";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
