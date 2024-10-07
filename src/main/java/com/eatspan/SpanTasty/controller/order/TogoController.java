package com.eatspan.SpanTasty.controller.order;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.eatspan.SpanTasty.entity.order.TogoEntity;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.service.order.TogoService;
import com.eatspan.SpanTasty.service.reservation.RestaurantService;

@Controller
@RequestMapping("/order")
public class TogoController {
	
	@Autowired
	private TogoService togoService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	//查詢
	// 有參數：模糊查詢，無參數：查詢全部
//	@GetMapping("/togo")
//	public ResponseEntity<List<TogoEntity>> getTogo(
//			@RequestParam(required = false) Integer memberId,
//			@RequestParam(required = false) String tgPhone) {
//		//有輸入memberId
//		if (memberId != null) {
//			List<TogoEntity> togo = togoService.getTogoByMemberId(memberId);
//			// 檢查memberId有沒有訂單資料，沒有:404，有:回傳訂單
//			if (togo.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//			}
//			return ResponseEntity.ok(togo);
//		}
//		//有輸入tgPhone
//		if (tgPhone != null) {
//			List<TogoEntity> togo = togoService.getTogoByPhone(tgPhone);
//			// 檢查tgPhone有沒有訂單資料，沒有:404，有:回傳訂單
//			if (togo.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//			}
//			return ResponseEntity.ok(togo);
//		}
//		return ResponseEntity.ok(togoService.getAllTogo());
//	}
	
	@GetMapping("/togo/get")
	public String getTogoPage() {
		return "order/getTogo";
	}
	@GetMapping("/togo/add")
	public String addTogoPage(Model model) {
		List<Restaurant> restaurants = restaurantService.findAllRestaurants();
		model.addAttribute("restaurants", restaurants);
		return "order/addTogo";
	}
	
	@GetMapping("/togo")
	public String getAllTogo(Model model) {
		List<TogoEntity> togoList = togoService.getAllTogo();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    for (TogoEntity togo : togoList) {
	        togo.setFormattedDate(togo.getTogoCreateTime().format(formatter)); // 將日期格式化為字串
	    }
		model.addAttribute("togoList", togoList);
		return "order/getAllTogo";
	}
	
	@GetMapping("/togo/{togoId}")
	@ResponseBody
	public ResponseEntity<TogoEntity> getTogoById(@PathVariable Integer togoId) {
		TogoEntity togo = togoService.getTogoById(togoId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		togo.setFormattedDate(togo.getTogoCreateTime().format(formatter));
		return ResponseEntity.ok(togo);
	}
	
	@PostMapping("/togo")
	@ResponseBody
	public ResponseEntity<List<TogoEntity>> addTogo(@RequestBody TogoEntity newTogo) {
		togoService.addTogo(newTogo);
		return ResponseEntity.ok(togoService.getAllTogo());
	}
	
	//刪除&更新
	@PutMapping("/togo/{togoId}")
	@ResponseBody
	public ResponseEntity<TogoEntity> updateTogoById(@PathVariable Integer togoId, @RequestBody TogoEntity updateTogo) {
		TogoEntity togo = togoService.getTogoById(togoId);
		if (togo == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		togoService.updateTogoById(togoId, updateTogo);
		return ResponseEntity.ok(togoService.getTogoById(togoId));
	}
	
}





