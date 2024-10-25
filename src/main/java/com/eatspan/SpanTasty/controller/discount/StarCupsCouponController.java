package com.eatspan.SpanTasty.controller.discount;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.discount.CouponDTO;
import com.eatspan.SpanTasty.dto.store.ShoppingItemDTO;
import com.eatspan.SpanTasty.entity.discount.Coupon;
import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.discount.Point;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.service.discount.CouponMemberService;
import com.eatspan.SpanTasty.service.discount.CouponService;
import com.eatspan.SpanTasty.service.discount.PointService;
import com.eatspan.SpanTasty.utils.account.JwtUtil;


@Controller
@RequestMapping("/StarCups")
public class StarCupsCouponController {
	
	@Autowired
	private CouponMemberService couponMemberService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private PointService pointService;
	
	@GetMapping("/coupon")
	public String showCoupon() {
		return "/starcups/discount/coupon2";
	}
	
	@GetMapping("/coupon/show")
	@ResponseBody
	public Map<String, List<CouponMember>> getMethodName(@RequestParam Integer memberId) {
		System.out.println("show");
		return couponMemberService.starCupsCoupon(memberId);
	}
	
	@PostMapping("/coupon/add")
	public ResponseEntity<?> addCoupon(@RequestBody Map<String,Object> jsonMap) {
		Integer memberId =(Integer)jsonMap.get("memberId") ;
		String couponCode = (String) jsonMap.get("couponCode") ;
		System.out.println(couponCode);
		try {
			Boolean canGetCoupon = couponService.canGetCoupon(memberId, couponCode);
			if(canGetCoupon) {
				couponService.getCoupon(memberId, couponCode);
				return ResponseEntity.ok("新增成功!");
			}else {
				return ResponseEntity.badRequest().body("無法新增優惠券，可能已達領取上限或優惠券不存在。");
			}			
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("處理請求時發生錯誤，請稍後再試。");
		}
	}
	
	@PostMapping("/coupon/checkout")
	@ResponseBody
	public List<CouponMember> getMethodName(@RequestBody ShoppingItemDTO shoppingItemDTO) {
		Integer memberId = shoppingItemDTO.getMemberId();
		Integer totalAmount = shoppingItemDTO.getTotalAmount();
		List<ShoppingItem> shoppingItems = shoppingItemDTO.getShoppingItems();
		
        return couponService.couponCanUse(shoppingItems, totalAmount, memberId);

	}
	
	@GetMapping("/coupon/discount")
	@ResponseBody
	public Integer coculateCouponDiscount(@RequestParam String couponCode,@RequestParam Integer totalAmount) {
		return couponService.coculateCouponDiscount(couponCode, totalAmount);
		
	}
	
	@GetMapping("/coupon/{id}")
	public String couponDetail(@PathVariable(value = "id")Integer couponId,Model model) {
		CouponDTO coupon = couponService.getCouponById(couponId);
//		model.addAttribute("coupon",coupon);
		return "/starcups/discount/couponDetail";
	}
	
	
	@PostMapping("/coupon/allDiscount")
	public ResponseEntity<?> postMethodName(@RequestBody Map<String,Integer> jsonMap) {
		Integer couponDiscount =jsonMap.get("couponDiscount")!= null? jsonMap.get("couponDiscount") : 0 ;
		Integer pointDiscount =jsonMap.get("pointDiscount")!= null? jsonMap.get("pointDiscount") : 0 ;
		Integer shoppingId =jsonMap.get("shoppingId") ;
		Integer memberId =jsonMap.get("memberId") ;
		Integer couopnId =jsonMap.get("couopnId") ;
		Integer finalAmount =jsonMap.get("finalAmount") ;
		Integer totalDiscount = couponDiscount+pointDiscount;
		
		try {
			//更新訂單折扣金額
			couponService.updateDiscountAmount(shoppingId, totalDiscount);
			//使用優惠券
			if(couponDiscount != 0) couponService.useCoupon(couopnId, memberId);
			//使用點數
			if(pointDiscount != 0) {
				//使用點數
				pointService.usePoint(pointDiscount, memberId);
				//新增點數紀錄
				Point point = new Point();
				point.setMemberId(memberId);
				point.setPointChange(pointDiscount*-1);
				point.setTransactionType("商城");
				point.setTransactionId(shoppingId);
				point.setCreateDate(new Date());
				pointService.insertOneRecord(point);				
			}
			//集點更新
			pointService.collectPoint(finalAmount, memberId, shoppingId,"商城");
									
			return ResponseEntity.ok("新增成功!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("處理請求時發生錯誤，請稍後再試。");
		}
	}
	
}
