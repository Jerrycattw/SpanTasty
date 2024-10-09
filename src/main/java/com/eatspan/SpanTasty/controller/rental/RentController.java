package com.eatspan.SpanTasty.controller.rental;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.rental.RentDetailDTO;
import com.eatspan.SpanTasty.dto.rental.RentKeywordDTO;
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
	public String toAddAndSearch(Model model) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		List<Member> members = memberService.findAllMembers();
		model.addAttribute("restaurants" ,restaurants);
		model.addAttribute("members" ,members);
		List<Tableware> tablewares = tablewareService.findAllTablewares();
		// 過濾掉 tablewareStatus == 2 的餐具
		List<Tableware> availableTablewares = tablewares.stream()
		        .filter(tableware -> tableware.getTablewareStatus() != 2)
		        .collect(Collectors.toList());
		
		model.addAttribute("tablewares" ,availableTablewares);
		return "spantasty/rental/addRent";
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
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
		for(RentItem rentItem: rentItems) {
			rentItemService.deleteRentItem(rentItem);
		}
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
			return "spantasty/rental/setRent";
			
		} else if ("return".equals(action)) {
			List<Restaurant> restaurants = restaurantService.findAllRestaurants();
			model.addAttribute("restaurants" ,restaurants);
			Date returnDate = new Date();
			rent.setReturnDate(returnDate);
			model.addAttribute("rent", rent);
			return "spantasty/rental/setRentReturn";
			
		}else if("get".equals(action)){
			List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rentId);
			model.addAttribute("rentItems",rentItems);
			return "spantasty/rental/getRentAndItems";
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
		rent.setRentStatus(2);
		rent.setRentMemo("已歸還");
		rentService.addRent(rent);
		
		List<RentItem> rentItems = rentItemService.findRentItemsByRentId(rent.getRentId());
		for(RentItem rentItem: rentItems) {
			rentItem.setReturnStatus(2);
			rentItem.setReturnMemo("完全歸還");
			rentItemService.addRentItem(rentItem);
		}
		return "redirect:/rent/getAll";
	}

	
	//查詢所有訂單
	@GetMapping("getAll")
	public String getAllRents(Model model, @RequestParam(value = "p", defaultValue = "1") Integer page) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		List<Member> members = memberService.findAllMembers();
		model.addAttribute("restaurants" ,restaurants);
		model.addAttribute("members" ,members);
		Page<Rent> rentPages = rentService.findAllRentPages(page );
		model.addAttribute("rentPages",rentPages);
		
		Map<Integer, String> restaurantMap = restaurants.stream()
		        .collect(Collectors.toMap(Restaurant::getRestaurantId, Restaurant::getRestaurantName));
		    Map<Integer, String> memberMap = members.stream()
		        .collect(Collectors.toMap(Member::getMemberId, Member::getMemberName));

		    // 使用分頁中的租借訂單進行 DTO 轉換
		    List<RentDetailDTO> rentDetails = rentPages.getContent().stream().map(rent -> {
		        RentDetailDTO dto = new RentDetailDTO();
		        dto.setRentId(rent.getRentId());
		        dto.setRentDeposit(rent.getRentDeposit());
		        dto.setRentDate(rent.getRentDate());
		        dto.setDueDate(rent.getDueDate());
		        dto.setReturnDate(rent.getReturnDate());
		        dto.setRentStatus(rent.getRentStatus());
		        dto.setRentMemo(rent.getRentMemo());
		        dto.setRestaurantId(rent.getRestaurantId());
		        dto.setMemberId(rent.getMemberId());
		        dto.setReturnRestaurantId(rent.getReturnRestaurantId());

		        // 從 Map 中查找對應的餐廳名稱和成員名稱
		        dto.setRestaurantName(restaurantMap.get(rent.getRestaurantId()));
		        dto.setMemberName(memberMap.get(rent.getMemberId()));
		        dto.setReturnRestaurantName(restaurantMap.get(rent.getReturnRestaurantId()));

	        
	        return dto;
		}).collect(Collectors.toList());
		
		model.addAttribute("rents" ,rentDetails);
		return "spantasty/rental/getAllRents";
	}
	
	
	//查詢訂單(By多個條件)
	@ResponseBody
	@PostMapping("/get")
	public ResponseEntity<List<RentDetailDTO>> getRents(@RequestBody RentKeywordDTO rentKeywordDTO) {
		try {
			List<Restaurant> restaurants = restaurantService.findAllRestaurants();
			List<Member> members = memberService.findAllMembers();
			
			Integer rentId = (rentKeywordDTO.getRentId() != null && rentKeywordDTO.getRentId() != 0) ? rentKeywordDTO.getRentId() : null;
	        Integer memberId = (rentKeywordDTO.getMemberId() != null && rentKeywordDTO.getMemberId() != 0) ? rentKeywordDTO.getMemberId() : null;
	        Integer restaurantId = (rentKeywordDTO.getRestaurantId() != null && rentKeywordDTO.getRestaurantId() != 0) ? rentKeywordDTO.getRestaurantId() : null;
	        Integer rentStatus = (rentKeywordDTO.getRentStatus() != null && rentKeywordDTO.getRentStatus() != 0) ? rentKeywordDTO.getRentStatus() : null;
	        Date rentDateStart = (rentKeywordDTO.getRentDateStart() != null && !rentKeywordDTO.getRentDateStart().trim().isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(rentKeywordDTO.getRentDateStart()) : null;
	        Date rentDateEnd = (rentKeywordDTO.getRentDateEnd() != null && !rentKeywordDTO.getRentDateEnd().trim().isEmpty()) ? new SimpleDateFormat("yyyy-MM-dd").parse(rentKeywordDTO.getRentDateEnd()) : null;
	        
			List<Rent> rents = rentService.findRentsByCriteria(rentId, memberId, restaurantId, rentStatus, rentDateStart, rentDateEnd);
			
			List<RentDetailDTO> rentDetails = rents.stream().map(rent -> {
				RentDetailDTO dto = new RentDetailDTO();
				dto.setRentId(rent.getRentId());
		        dto.setRentDeposit(rent.getRentDeposit());
		        dto.setRentDate(rent.getRentDate());
		        dto.setDueDate(rent.getDueDate());
		        dto.setReturnDate(rent.getReturnDate());
		        dto.setRentStatus(rent.getRentStatus());
		        dto.setRentMemo(rent.getRentMemo());
		        dto.setRestaurantId(rent.getRestaurantId());
		        dto.setMemberId(rent.getMemberId());
		        dto.setReturnRestaurantId(rent.getReturnRestaurantId());
		        
		        restaurants.stream()
		            .filter(r -> r.getRestaurantId().equals(rent.getRestaurantId()))
		            .findFirst()
		            .ifPresent(r -> dto.setRestaurantName(r.getRestaurantName()));

	        // 查找對應的成員名稱
		        members.stream()
		            .filter(m -> m.getMemberId().equals(rent.getMemberId()))
		            .findFirst()
		            .ifPresent(m -> dto.setMemberName(m.getMemberName()));
		        
		        restaurants.stream()
			        .filter(r -> r.getRestaurantId().equals(rent.getReturnRestaurantId()))
			        .findFirst()
			        .ifPresent(r -> dto.setReturnRestaurantName(r.getRestaurantName()));
		        
		        return dto;
			}).collect(Collectors.toList());
			
			return ResponseEntity.ok(rentDetails);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	
	//查詢訂單(By過期未歸還)
	@GetMapping("/overtime")
	public String getOvertimeRents(Model model) {
		List<Rent> rents = rentService.findOvertimeRents();
		model.addAttribute("rents", rents);
		return "spantasty/rental/getAllRents";
	}
}
