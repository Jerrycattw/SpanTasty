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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.rental.RentItemService;
import com.eatspan.SpanTasty.service.rental.RentService;
import com.eatspan.SpanTasty.service.rental.TablewareService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

@Controller
@RequestMapping("/rent")
public class RentController {
	
	@Autowired
	private RentService rentService;
	@Autowired
	private TablewareService tablewareService;
	@Autowired
	private RentItemService rentItemService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private MemberService memberService;
	
	
	//查詢下拉式選單
	@GetMapping("/add")
	public String toAddAndSearch(@RequestParam(name = "action") String action , Model model) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		List<Member> members = memberService.findAllMembers();
		model.addAttribute("restaurants" ,restaurants);
		model.addAttribute("members" ,members);
		if("add".equals(action)) {
			List<Tableware> tablewares = tablewareService.findAllTablewares();
			model.addAttribute("tablewares" ,tablewares);
			return "rental/addRent";
			
		}else if ("get".equals(action)) {
			List<Rent> rents = rentService.findAllRents();
			model.addAttribute("rents" ,rents);
			return "rental/getRents";
		}
		return null;
	}
	
	
	//新增訂單 訂單明細
	@PostMapping("/addPost")
	public String addRentAndRentItems(
			@ModelAttribute Rent rent,
			@RequestParam Map<String, String> allParams, 
			Model model) {
		Date rentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rentDate);
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		Date dueDate = calendar.getTime();
		rent.setRentDate(rentDate);
		rent.setDueDate(dueDate);
		rent.setRentStatus(1);
		rent.setRentMemo("未歸還");
		rentService.addRent(rent);
		
		int rentId = rent.getRentId();
		// 保存 RentItem 資料
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
	        rentItemService.addRentItem(rentItem);
	    }
		return "redirect:/rent/getAll";
	}
	
	
	//刪除訂單 訂單明細
	@DeleteMapping("/del/{id}")
	public String deleteRentAndRentItems(@PathVariable("id") Integer rentId, Model model) {
		rentItemService.deleteRentItems(rentId);
		rentService.deleteRent(rentId);
		return "redirect:/rent/getAll";
	}
	
	

	//查詢訂單(By訂單編號)
	@GetMapping("/set/{id}")
	public String toSetRent(@PathVariable("id") Integer rentId, @RequestParam("action") String action, Model model) {
		Rent rent = rentService.findRentById(rentId);
		model.addAttribute("rent", rent);
		if ("update".equals(action)) {
			List<Restaurant> restaurants = restaurantService.findAllRestaurants();
			model.addAttribute("restaurants" ,restaurants);
			List<Member> members = memberService.findAllMembers();
			model.addAttribute("members" ,members);
			return "rental/setRent";
			
		} else if ("return".equals(action)) {
			List<Restaurant> restaurants = restaurantService.findAllRestaurants();
			model.addAttribute("restaurants" ,restaurants);
			Date returnDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(returnDate);
			return "rental/setRentReturn";
			
		}else if("get".equals(action)){
			List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
			model.addAttribute("rentItems",rentItems);
			return "rental/getRentAndItems";
		}
		return null;
	}
	
	
	//更改訂單
	@PutMapping("/setPut1")
	protected String updateRent(@ModelAttribute Rent rent, Model model) {
		rentService.addRent(rent);
		return "redirect:/rent/getAll";
	}
	
	
	//歸還訂單
	@PutMapping("/setPut2")
	public String returnRent(@ModelAttribute Rent rent, Model model) {
		try {
			Date returnDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(returnDate);
			rent.setReturnDate(returnDate);
			rentService.addRent(rent);
			return "redirect:/rent/getAll";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//查詢所有訂單
	@GetMapping("getAll")
	public String getAllRents(Model model) {
		List<Rent> rents = rentService.findAllRents();
		model.addAttribute("rents",rents);
		return "rental/getAllRents";
	}
	
	
	//查詢訂單(By多個條件)
	@GetMapping("/get")
	public String getRentsBySearch(
			@ModelAttribute Rent rent,
			@RequestParam(value = "rentDateStart", required = false) String rentDateStartStr,
	        @RequestParam(value = "rentDateEnd", required = false) String rentDateEndStr,
			Model model) {
		try {
			Integer rentId = rent.getRentId() != null ? rent.getRentId() : null;
			Integer memberId = rent.getMemberId() != null ? rent.getMemberId() : null;
			Integer restaurantId = rent.getRestaurantId() != null ? rent.getRestaurantId() : null;
			Integer rentStatus = rent.getRentStatus() != null ? rent.getRentStatus() : null;
	        Date rentDateStart = (rentDateStartStr != null && !rentDateStartStr.isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(rentDateStartStr) : null;
	        Date rentDateEnd = (rentDateEndStr != null && !rentDateEndStr.isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(rentDateEndStr) : null;
	        
			List<Rent> rents = rentService.findRentsByCriteria(rentId, memberId, restaurantId, rentStatus, rentDateStart, rentDateEnd);
			model.addAttribute("rents", rents);
			return "rental/getRents";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//查詢訂單(By過期未歸還)
	@GetMapping("/overtime")
	public String getOvertimeRents(Model model) {
		List<Rent> rents = rentService.findOvertimeRents();
		model.addAttribute("rents", rents);
		return "rental/getAllRents";
	}
}
