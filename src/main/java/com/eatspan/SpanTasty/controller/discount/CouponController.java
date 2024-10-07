package com.eatspan.SpanTasty.controller.discount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.dto.discount.CouponDTO;
import com.eatspan.SpanTasty.dto.discount.CouponDistributeDTO;
import com.eatspan.SpanTasty.entity.discount.Coupon;
import com.eatspan.SpanTasty.service.discount.CouponService;

@Controller
//@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	private CouponService couponService;
	
	
	//首頁--------------------------------
	@GetMapping("/coupon")//導向
	public String couponHome() {
		return "discount/coupon/coupon";
	}
	
	@GetMapping("/coupon/show")
	@ResponseBody
	public List<CouponDTO> getAllCouponWithTagsAndReceived() {
		return couponService.getAllCoupon();
	}
	
	//編輯---------------------------------
	@GetMapping("/coupon/{id}")//導向
	public String couponEddit(@PathVariable(value = "id") Integer couponId) {
		return"discount/coupon/updateCoupon";
	}
	
	@PostMapping("/coupon/{id}/show")
	@ResponseBody
	public Map<String, Object> getOneCouponWithTags(@PathVariable(value = "id") Integer couponId) {
		System.out.println("touch");
 		CouponDTO couponDTO = couponService.getCouponById(couponId);
 		Map<String, List<String>> tagOptions = couponService.getTagOptions();
 		
 		Map<String, Object> data = new HashMap<String, Object>();
 		data.put("coupon", couponDTO);
 		data.put("tagOptions", tagOptions);
 		return data;
 	}
	
 	@PutMapping("/coupon/{id}/post")
 	private String updateCouponWithTags(Coupon couponBean,
 										@PathVariable(value = "id") Integer couponId,
 										@RequestParam(value = "product",required = false) String[] productTags,
										@RequestParam(value = "togo",required = false) String[] togoTags) {
		couponBean.setCouponId(couponId);
		couponService.updateCoupon(couponBean,productTags,togoTags);
		return "redirect:/coupon";
 	}
 	
 	//新增---------------------------------
 	@GetMapping("/coupon/add")//導向
 	public String couponAdd() {
 		return "discount/coupon/insertCoupon";
 	}
 	
// 	@PostMapping("/coupon/preAdd")//生成空input
// 	@ResponseBody
// 	public Map<String, Object> getDefaultCouponWithTags(){
// 		CouponDTO couponDTO=new CouponDTO();
// 		Map<String, List<String>> tagOptions = couponService.getTagOptions();
// 		
// 		Map<String, Object> data = new HashMap<String, Object>();
// 		data.put("coupon", couponDTO);
// 		data.put("tagOptions", tagOptions);
// 		return data;
// 	}
 	
 	@GetMapping("/coupon/api/preadd")
 	@ResponseBody
 	public Map<String, List<String>> getDefaultCouponWithTagsApi() {
 		return couponService.getTagOptions();
 	}
 	
 	@PostMapping("/coupon/add")
 	public String insertCouponWithTags(Coupon couponBean, 
 										@RequestParam(value = "product",required = false) String[] productTags,
 										@RequestParam(value = "togo",required = false) String[] togoTags) {
 		couponService.insertCoupon(couponBean,productTags,togoTags);
 		return "redirect:/coupon";
 	}
 	
 	//刪除-------------------------------------
 	@DeleteMapping("/coupon/{id}")
 	@ResponseBody
 	public ResponseEntity<?> deleteCoupon(@PathVariable(value = "id") Integer couponId) {
 		couponService.deleteCouponById(couponId);
 		return ResponseEntity.ok("刪除成功!");
 	}
 	
 	//search
 	@GetMapping("/coupon/search")
 	@ResponseBody
 	public List<CouponDTO> searchCoupon(@RequestParam("search") String keyWord){
 		List<CouponDTO> coupons = couponService.searchCoupons(keyWord);
 		return coupons;
 	}
 	//發放--------------------------------
 	@PostMapping("/coupon/distribute")
 	@ResponseBody
 	public List<CouponDistributeDTO> distributeCoupon(@RequestBody String memberIds) {
 		return couponService.getDistributeOption(memberIds);
 	}
 	
 	@PostMapping("/coupon/distribute/inf")
 	@ResponseBody
 	public CouponDTO distributeCouponInf(@RequestBody String couponIdStr) {
 		int couponId = Integer.parseInt(couponIdStr);
 		return couponService.getCouponById(couponId);
 	}
 	
 	@PostMapping("/coupon/distribute/post")
 	@ResponseBody
 	public List<CouponDistributeDTO> distributeCouponPost(@RequestBody Map<String,Object> jsonMap ) {
 		String memberIds =(String) jsonMap.get("memberIds");
 		Integer couponId =Integer.parseInt((String) jsonMap.get("couponId")) ;
 		Integer perMaxCoupon =(Integer) jsonMap.get("perMaxCoupon");
 		System.out.println("XXXX"+memberIds+" "+couponId+" "+perMaxCoupon);
 		
 		List<CouponDistributeDTO> distributeResult = couponService.distributeExcute2(memberIds, couponId, perMaxCoupon);
 		return distributeResult;
 	}
 	
}
