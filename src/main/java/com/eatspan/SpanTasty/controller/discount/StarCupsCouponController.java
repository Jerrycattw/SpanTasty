package com.eatspan.SpanTasty.controller.discount;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.store.ShoppingItemDTO;
import com.eatspan.SpanTasty.entity.discount.Coupon;
import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.service.discount.CouponMemberService;
import com.eatspan.SpanTasty.service.discount.CouponService;
import com.eatspan.SpanTasty.utils.account.JwtUtil;


@Controller
@RequestMapping("/StarCups")
public class StarCupsCouponController {
	
	@Autowired
	private CouponMemberService couponMemberService;
	
	@Autowired
	private CouponService couponService;
	
	@GetMapping("/coupon")
	public String showCoupon() {
		return "/starcups/discount/coupon";
	}
	
	@GetMapping("/coupon/show")
	@ResponseBody
	public Map<String, List<CouponMember>> getMethodName(@RequestParam Integer memberId) {
		return couponMemberService.starCupsCoupon(memberId);
	}
	
	@PostMapping("/coupon/add")
	public ResponseEntity<?> addCoupon(@RequestBody Map<String,Object> jsonMap) {
		Integer memberId =(Integer)jsonMap.get("memberId") ;
		String couponCode = (String) jsonMap.get("couponCode") ;
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
	public List<CouponMember> getMethodName(@RequestHeader(value = "Authorization") String token,@RequestBody ShoppingItemDTO shoppingItemDTO) {
		Map<String, Object> claims = JwtUtil.parseToken(token);
        Integer memberId = (Integer) claims.get("memberId"); 
        
        return couponService.couponCanUse(shoppingItemDTO.getShoppingItems(), shoppingItemDTO.getTotalAmount(), memberId);

	}
	
}
