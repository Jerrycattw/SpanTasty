package com.eatspan.SpanTasty.controller.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.eatspan.SpanTasty.dto.reservation.ReserveCheckDTO;
import com.eatspan.SpanTasty.dto.reservation.ReserveDTO;
import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.service.reservation.ReserveService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;
import com.eatspan.SpanTasty.service.reservation.RestaurantTableService;
import com.eatspan.SpanTasty.service.reservation.TableTypeService;

@Controller
@RequestMapping("/reserve")
public class ReserveController {
	
	
	@Autowired
	private ReserveService reserveService;
	@Autowired
	private RestaurantTableService restaurantTableService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private TableTypeService tableTypeService;
	@Autowired
	private MemberService memberService;
	
	
	
	// 導向到查詢訂單頁面
    @GetMapping("/getAll")
    public String showReserve(Model model) {
        List<TableType> tableTypes = tableTypeService.findAllTableType();
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        model.addAttribute("tableTypes", tableTypes);
        model.addAttribute("restaurants", restaurants);
        return "spantasty/reservation/getAllReserve";
    }
	
    // 導向到訂位中心頁面
    @GetMapping("/reserveCenter")
    public String showReserveCenter(Model model) {
    	List<Restaurant> restaurants = restaurantService.findAllRestaurants();
    	model.addAttribute("restaurants", restaurants);
    	return "spantasty/reservation/reserveCenter";
    }
    
    

    
    
    
	// 修改訂位參數的ajax
	@PutMapping("/setReserveCenter")
	@ResponseBody
	public String updateReserveCenter(@RequestBody Restaurant newRestaurant) {
		System.out.println(newRestaurant.getRestaurantId());
		System.out.println(newRestaurant.getReservePercent());
		Restaurant restaurant = restaurantService.findRestaurantById(newRestaurant.getRestaurantId());
		restaurant.setReservePercent(newRestaurant.getReservePercent());
		restaurant.setReserveTimeScale(newRestaurant.getReserveTimeScale());
		restaurantService.updateRestaurant(restaurant);
		return "ReserveCenter update ok";
	}
    
    
    // ajax 查詢訂位訂單
    @GetMapping("/getList")
    @ResponseBody
    public List<Reserve> getListReserve(@RequestParam(required = false) String memberName,
							            @RequestParam(required = false) String phone,
							            @RequestParam(required = false) Integer restaurantId,
							            @RequestParam(required = false) String tableTypeId,
							            @RequestParam(required = false) LocalDateTime reserveTimeStart,
							            @RequestParam(required = false) LocalDateTime reserveTimeEnd) {
    	
    	List<Reserve> reserveByCriteria = reserveService.findReserveByCriteria(memberName, phone, restaurantId, tableTypeId, reserveTimeStart, reserveTimeEnd);
    	return reserveByCriteria;
    }
    
    
    // ajax 查詢所有餐廳特定時間內訂位訂單數量
    @GetMapping("/getReserveSum")
    @ResponseBody
    public Map<String, Integer> getReserveSum(@RequestParam(required = false) LocalDate slotEndDate,
    								   		  @RequestParam(required = false) LocalDate slotStartDate) {
    	Map<String, Integer> reserveSum = reserveService.getReserveSum(null, null);
    	return reserveSum;
    }
    
    
    // ajax 查詢所有訂位訂單數量byMonth
    @GetMapping("/getReserveMonth")
    @ResponseBody
    public List<Integer> getReserveMonth(@RequestParam(required = false) Integer year) {
    	List<Integer> reserveSum = reserveService.getReserveInMonth(year);
    	return reserveSum;
    }
    
    
    
    // ajax查詢可訂位時段
    @GetMapping("/getReserveCheck")
    @ResponseBody
    public List<ReserveCheckDTO> getReserveCheck(@RequestParam Integer restaurantId,
									    		 @RequestParam Integer reserveSeat,
									    		 @RequestParam LocalDate checkDate) {
    	
    	String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserveSeat);
    	List<ReserveCheckDTO> reserveCheck = reserveService.getReserveCheck(restaurantId, tableTypeId, checkDate);
    	return reserveCheck;
    }
    
    
    
    
	@DeleteMapping("/del")
	@ResponseBody
	public String delTable(@RequestParam Integer reserveId) {
		reserveService.deleteReserve(reserveId);
		return "delete ok";
	}
    
    
	
	
	// 新增訂位的ajax
	@PostMapping("/add")
	@ResponseBody
	public String addReserve(@RequestBody ReserveDTO reserveDTO) {
		
	    Reserve reserve = new Reserve();
	    
	    System.out.println(reserveDTO.getCheckDate());
	    System.out.println(reserveDTO.getStartTime());
	    
	    reserve.setReserveSeat(reserveDTO.getReserveSeat());
	    reserve.setReserveTime(reserveDTO.getCheckDate().atTime(reserveDTO.getStartTime()));
	    
	    // 設定member外鍵關聯
	    Member member = memberService.findMemberById(reserveDTO.getMemberId())
	        .orElseThrow(() -> new RuntimeException("Member not found"));
	    reserve.setMember(member);
	    
	    
	    // 設定restaurant外鍵關聯
	    Restaurant restaurant = restaurantService.findRestaurantById(reserveDTO.getRestaurantId());
		if (restaurant == null) {
		    throw new RuntimeException("Restaurant not found");
		}
	    reserve.setRestaurant(restaurant);
	    
	    
	    // 根據reserveSeat取得訂位桌子種類
	    String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserve.getReserveSeat());
	    // 設定tableType外鍵關聯
	    TableType tableType = tableTypeService.findTableTypeById(tableTypeId);
	    if (tableType == null) {
	    	throw new RuntimeException("TableType not found");
	    }
	    reserve.setTableType(tableType);
	    
	    
	    // 保存訂位
	    reserveService.addReserve(reserve);
		
		return "Reserve add ok";
	}
	
	
	
    // ajax 查詢訂位訂單
    @GetMapping("/get/{id}")
    @ResponseBody
    public Reserve getReserve(@PathVariable Integer id) {
    	return reserveService.findReserveById(id);
    }
	
	// 修改訂位的ajax
	@PutMapping("/set")
	@ResponseBody
	public String updateReserve(@RequestBody ReserveDTO reserveDTO) {
		
		System.out.println(reserveDTO.getReserveId());
	    Reserve reserve = reserveService.findReserveById(reserveDTO.getReserveId());
	    
	    System.out.println(reserveDTO.getCheckDate());
	    System.out.println(reserveDTO.getStartTime());
	    
	    reserve.setReserveSeat(reserveDTO.getReserveSeat());
	    reserve.setReserveTime(reserveDTO.getCheckDate().atTime(reserveDTO.getStartTime()));
	    
	    // 設定member外鍵關聯
	    Member member = memberService.findMemberById(reserveDTO.getMemberId())
	        .orElseThrow(() -> new RuntimeException("Member not found"));
	    reserve.setMember(member);
	    
	    
	    // 設定restaurant外鍵關聯
	    Restaurant restaurant = restaurantService.findRestaurantById(reserveDTO.getRestaurantId());
		if (restaurant == null) {
		    throw new RuntimeException("Restaurant not found");
		}
	    reserve.setRestaurant(restaurant);
	    
	    
	    // 根據reserveSeat取得訂位桌子種類
	    String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserve.getReserveSeat());
	    // 設定tableType外鍵關聯
	    TableType tableType = tableTypeService.findTableTypeById(tableTypeId);
	    if (tableType == null) {
	    	throw new RuntimeException("TableType not found");
	    }
	    reserve.setTableType(tableType);
	    
	    
	    // 保存訂位
	    reserveService.updateReserve(reserve);
		
		return "Reserve update ok";
		
	}
	
	// 修改訂位狀態的ajax
	@PutMapping("/setStatus")
	@ResponseBody
	public String updateReserveStatus(@RequestBody Reserve newReserveStatus) {
		Reserve reserve = reserveService.findReserveById(newReserveStatus.getReserveId());
		reserve.setReserveStatus(newReserveStatus.getReserveStatus());
		reserveService.updateReserve(reserve);
		return "ReserveStatus update ok";
	}
    
    
	
	
}
