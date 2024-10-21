package com.eatspan.SpanTasty.service.discount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.discount.Tag;
import com.eatspan.SpanTasty.repository.discount.CouponMemberRepository;

@Service
public class CouponMemberService {
	
	@Autowired
	private CouponMemberRepository couponMemberRepo;
	
	
	public Map<String, List<CouponMember>> starCupsCoupon(Integer memberId) {
		Map<String, List<CouponMember>> couponMap = new HashMap<>();
		//all
		List<CouponMember> coupons = couponMemberRepo.findByMemberId(memberId).stream().filter(couponMember->couponMember.getUsageAmount() > 0).collect(Collectors.toList());
		couponMap.put("all", coupons);	
		
		//togo,shop分類
		List<CouponMember> couponShop = new ArrayList<>();
		List<CouponMember> couponTogo = new ArrayList<>();
		for (CouponMember couponMember : coupons) {
			List<Tag> tags = couponMember.getCoupon().getTags();
			boolean isShop = tags.stream().anyMatch(tag->"product".equals(tag.getTagId().getTagType()));
			boolean isTogo = tags.stream().anyMatch(tag->"togo".equals(tag.getTagId().getTagType()));
			boolean noTags = tags.isEmpty();
			
			if (noTags || (isShop && isTogo) ) { //noTags表示沒有使用限制
				couponTogo.add(couponMember);
				couponShop.add(couponMember);
			}else if(isTogo){
				couponTogo.add(couponMember);
			}else if(isShop) {
				couponShop.add(couponMember);
			}
		}
		couponMap.put("shop", couponShop);
		couponMap.put("togo", couponTogo);
		return couponMap;
	}
}
