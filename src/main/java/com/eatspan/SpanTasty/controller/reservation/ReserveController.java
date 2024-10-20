package com.eatspan.SpanTasty.controller.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.eatspan.SpanTasty.dto.reservation.ReserveCenterDTO;
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
	
    // 導向到訂位統計頁面
    @GetMapping("/reserveStatic")
    public String showReserveStatic(Model model) {
    	List<Restaurant> restaurants = restaurantService.findAllRestaurants();
    	model.addAttribute("restaurants", restaurants);
    	return "spantasty/reservation/reserveStatic";
    }
    
    
    // 導向到餐廳訂位中心頁面
//    @GetMapping("/reserveCenter/{id}")
//    public String showReserveCenter(Model model, @PathVariable Integer id) {
//    	model.addAttribute("restaurant", restaurantService.findRestaurantById(id));
//    	return "spantasty/reservation/reserveCenter";
//    }
    
    // 查詢餐廳訂位顯示在訂位中心頁面
//    @GetMapping("/reserveCenter/api/{id}")
//    public ResponseEntity<?> showReserveCenterApi(@PathVariable Integer id,
//    											  @RequestParam(required = false) LocalDate checkDate) {
//        // 檢查餐廳ID的有效性
//        if (id == null || id <= 0) return ResponseEntity.badRequest().body("RestaurantId Not Found");
//    	if(checkDate == null) checkDate = LocalDate.now();
//    	List<Reserve> reserves = reserveService.findReserveByRestaurantAndDate(id, checkDate);
//    	List<RestaurantTable> restaurantTables = restaurantTableService.findAllRestaurantTable(id);
//    	ReserveCenterDTO reserveCenterDTO = new ReserveCenterDTO(reserves, restaurantTables);
//    	
//    	return ResponseEntity.ok(reserveCenterDTO);
//    }
    
    // 修改餐廳訂位參數的ajax
//	@PutMapping("/setReserveCenter")
//	public ResponseEntity<?> updateReserveRule(@RequestBody Restaurant newRestaurant) {
//		
//		Restaurant restaurant = restaurantService.findRestaurantById(newRestaurant.getRestaurantId());
//		restaurant.setReservePercent(newRestaurant.getReservePercent());
//		restaurant.setReserveTimeScale(newRestaurant.getReserveTimeScale());
//		restaurantService.updateRestaurant(restaurant);
//		return ResponseEntity.ok("ReserveCenter update ok");
//	}
    

    
    
    
    
    
    // ajax 查詢訂位訂單
    @GetMapping("/getList")
    public ResponseEntity<?> getReserveList(@RequestParam(required = false) String memberName,
								            @RequestParam(required = false) String phone,
								            @RequestParam(required = false) Integer restaurantId,
								            @RequestParam(required = false) String tableTypeId,
								            @RequestParam(required = false) LocalDate reserveTimeStart,
								            @RequestParam(required = false) LocalDate reserveTimeEnd) {
    	
    	List<Reserve> reserveByCriteria = reserveService.findReserveByCriteria(memberName, phone, restaurantId, tableTypeId, reserveTimeStart, reserveTimeEnd);
    	return ResponseEntity.ok(reserveByCriteria);
    }
    
    // ajax查詢可訂位時段
    @GetMapping("/getReserveCheck")
    public ResponseEntity<?> getReserveCheck(@RequestParam Integer restaurantId,
									    	 @RequestParam Integer reserveSeat,
									    	 @RequestParam LocalDate checkDate) {
    	
    	String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserveSeat);
    	List<ReserveCheckDTO> reserveCheck = reserveService.getReserveCheck(restaurantId, tableTypeId, checkDate);
    	return ResponseEntity.ok(reserveCheck);
    }
    
    
    // 刪除訂位Ajax
	@DeleteMapping("/del")
	public ResponseEntity<?> delReserve(@RequestParam Integer reserveId) {
		reserveService.deleteReserve(reserveId);
		return ResponseEntity.ok("delete ok");
	}
    
    
	
	
	// 新增訂位的ajax
	@PostMapping("/add")
	public ResponseEntity<?> addReserve(@RequestBody ReserveDTO reserveDTO) {
		
	    Reserve reserve = new Reserve();
	    reserve.setReserveSeat(reserveDTO.getReserveSeat());
	    reserve.setReserveTime(reserveDTO.getCheckDate().atTime(reserveDTO.getStartTime()));
	    
	    // 設定member外鍵關聯
	    Member member = memberService.findMemberById(reserveDTO.getMemberId()).get();
	    if(member!=null) {
	    	reserve.setMember(member);	    	
	    }
	    
	    // 設定restaurant外鍵關聯
	    Restaurant restaurant = restaurantService.findRestaurantById(reserveDTO.getRestaurantId());
		if (restaurant == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Restaurant not found");
	    reserve.setRestaurant(restaurant);
	    
	    // 根據reserveSeat取得訂位桌子種類
	    String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserve.getReserveSeat());
	    
	    // 設定tableType外鍵關聯
	    TableType tableType = tableTypeService.findTableTypeById(tableTypeId);
	    if (tableType == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TableType not found");
	    reserve.setTableType(tableType);
	    
	    reserve.setReserveNote(reserveDTO.getReserveNote());
	    
	    // 保存訂位
	    reserveService.addReserve(reserve);
		
		return ResponseEntity.ok("reserve add success");
	}
	
	
	
    // ajax 查詢訂位訂單
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getReserve(@PathVariable Integer id) {
    	Reserve reserve = reserveService.findReserveById(id);
    	return ResponseEntity.ok(reserve);
    }
	
	// 修改訂位的ajax
	@PutMapping("/set")
	public ResponseEntity<?> updateReserve(@RequestBody ReserveDTO reserveDTO) {
		
	    Reserve reserve = reserveService.findReserveById(reserveDTO.getReserveId());
	    
	    reserve.setReserveSeat(reserveDTO.getReserveSeat());
	    reserve.setReserveTime(reserveDTO.getCheckDate().atTime(reserveDTO.getStartTime()));
	    
	    // 設定member外鍵關聯
	    Member member = memberService.findMemberById(reserveDTO.getMemberId()).orElseThrow(() -> new RuntimeException("Member not found"));
	    reserve.setMember(member);
	    
	    // 設定restaurant外鍵關聯
	    Restaurant restaurant = restaurantService.findRestaurantById(reserveDTO.getRestaurantId());
		if (restaurant == null) throw new RuntimeException("Restaurant not found");
	    reserve.setRestaurant(restaurant);
	    
	    // 根據reserveSeat取得訂位桌子種類
	    String tableTypeId = reserveService.getTableTypeIdByReserveSeat(reserve.getReserveSeat());
	    // 設定tableType外鍵關聯
	    TableType tableType = tableTypeService.findTableTypeById(tableTypeId);
	    if (tableType == null) throw new RuntimeException("TableType not found");
	    reserve.setTableType(tableType);
	    
	    reserve.setReserveNote(reserveDTO.getReserveNote());
	    
	    // 保存訂位
	    reserveService.updateReserve(reserve);
		
		return ResponseEntity.ok("Reserve update ok");
		
	}
	
	// 修改訂位狀態的ajax
	@PutMapping("/setStatus")
	public ResponseEntity<?> updateReserveStatus(@RequestBody Reserve newReserveStatus) {
		Reserve reserve = reserveService.findReserveById(newReserveStatus.getReserveId());
		reserve.setReserveStatus(newReserveStatus.getReserveStatus());
		reserveService.updateReserve(reserve);
		return ResponseEntity.ok("ReserveStatus update ok");
	}
	
	
	
	
	
	
	
	
	
	
	
    // ajax 查詢所有餐廳特定時間內訂位訂單數量
    @GetMapping("/getReserveSum")
    public ResponseEntity<?> getReserveSum(@RequestParam(required = false) LocalDate slotEndDate,
    								   	   @RequestParam(required = false) LocalDate slotStartDate) {
    	Map<String, Integer> reserveSum = reserveService.getReserveSum(null, null);
    	return ResponseEntity.ok(reserveSum);
    }
    
    
    // ajax 查詢所有訂位訂單數量byMonth
    @GetMapping("/getReserveMonth")
    public ResponseEntity<?> getReserveMonth(@RequestParam(required = false) Integer year) {
    	List<Integer> reserveSum = reserveService.getReserveInMonth(year);
    	return ResponseEntity.ok(reserveSum);
    }
    
    
	
	
}
