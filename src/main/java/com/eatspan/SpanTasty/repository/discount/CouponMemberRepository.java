package com.eatspan.SpanTasty.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.discount.CouponMemberId;

public interface CouponMemberRepository extends JpaRepository<CouponMember, CouponMemberId> {
	
}
