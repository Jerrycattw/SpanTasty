package com.eatspan.SpanTasty.service.discount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.repository.discount.CouponMemberRepository;

@Service
public class CouponMemberService {
	
	@Autowired
	private CouponMemberRepository couponMemberRepo;
	
	
	public Map<String, List<CouponMember>> starCupsCoupon(Integer memberId) {
		Map<String, List<CouponMember>> couponMap = new HashMap<>();
		//all
		List<CouponMember> coupons = couponMemberRepo.findByMemberMemberId(memberId);
		couponMap.put("all", coupons);				
		
		return couponMap;
	}
}
