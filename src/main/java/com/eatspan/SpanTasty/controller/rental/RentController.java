package com.eatspan.SpanTasty.controller.rental;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	
	//新增訂單 訂單明細
	@PostMapping("/add")
	public String addRentAndRentItems(@ModelAttribute Rent rent, Model model) {
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
	    // 保存 RentItem 列表資料
	    for (RentItem rentItem : rent.getRentItems()) {
	        rentItem.setRentId(rent.getRentId()); 
	        rentItem.setReturnMemo("未歸還");
	        rentItem.setReturnStatus(1);
	        rentItemService.addRentItem(rentItem);
	    }
		return "redirect:/rent/showAll";
	}
	
	
	//刪除訂單 訂單明細
	@DeleteMapping("/delete/{id}")
	public String deleteRentAndRentItems(@PathVariable("id") Integer rentId, Model model) {
		if(rentService.findRentById(rentId) != null) {
			rentItemService.deleteRentItems(rentId);
			rentService.deleteRent(rentId);
		}
		return "redirect:/rent/showAll";
	}
	
	
	//更改訂單
	@PutMapping("/update")
	protected String updateRent(@ModelAttribute Rent rent, Model model) {
		rentService.addRent(rent);
		return "redirect:/rent/showAll";
	}

	
	//查詢所有訂單
	@GetMapping("showAll")
	public String getAllRents(Model model) {
		List<Rent> rents = rentService.findAllRents();
		model.addAttribute("rents",rents);
		return "rental/ShowAllRents";
	}
	
	
	//查詢下拉式選單
	@GetMapping("option")
	public String getRentOption(@RequestParam(name = "action") String action , Model model) {
		//暫時修改
		List<Restaurant> restaurants = (List<Restaurant>) restaurantService.findAllRestaurants(1);
		List<Member> members = memberService.findAllMembers();
		model.addAttribute("restaurants" ,restaurants);
		model.addAttribute("members" ,members);
		if("add".equals(action)) {
			List<Tableware> tablewares = tablewareService.findAllTablewares();
			model.addAttribute("tablewares" ,tablewares);
			return "rental/addRent";
		}else if ("search".equals(action)) {
			List<Rent> rents = rentService.findAllRents();
			model.addAttribute("rents" ,rents);
			return "rental/searchRents";
		}
		return null;
	}

	
	//查詢訂單(By訂單編號)
	@GetMapping("/get/{id}")
	public String getById(@PathVariable("id") Integer rentId, @RequestParam("action") String action, Model model) {
		//暫時修改
		List<Restaurant> restaurants = (List<Restaurant>) restaurantService.findAllRestaurants(1);
		model.addAttribute("restaurants" ,restaurants);
		Rent rent = rentService.findRentById(rentId);
		model.addAttribute("rent", rent);
		if ("update".equals(action)) {
			List<Member> members = memberService.findAllMembers();
			model.addAttribute("members" ,members);
			return "rental/updateRent";
		} else if ("return".equals(action)) {
			Date returnDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(returnDate);
			return "rental/returnRent";
		}
		return null;
	}
	
	
	//查詢訂單(By多個條件)
	@GetMapping("/search")
	public String findRentsBySearch(
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
			return "rental/ShowAllRents";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//查詢訂單(By過期未歸還)
	@GetMapping("/overtime")
	public String findOvertimeRents(Model model) {
		List<Rent> rents = rentService.findOvertimeRents();
		model.addAttribute("rents", rents);
		return "rental/ShowAllRents";
	}
	
	
	
	@PutMapping("/return")
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
			rentService.addRent(rent);
			return "redirect:/rent/showAll";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
