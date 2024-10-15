package com.eatspan.SpanTasty.repository.discount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.discount.CouponMemberId;
import com.eatspan.SpanTasty.entity.account.Member;


public interface CouponMemberRepository extends JpaRepository<CouponMember, CouponMemberId> {
	
	List<CouponMember> findByMemberMemberId(Integer memberId);
}
