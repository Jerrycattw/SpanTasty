package com.eatspan.SpanTasty.repository.discount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.discount.CouponMember;
import com.eatspan.SpanTasty.entity.discount.CouponMemberId;
import com.eatspan.SpanTasty.entity.account.Member;


public interface CouponMemberRepository extends JpaRepository<CouponMember, CouponMemberId> {
	
	@Query("SELECT cm FROM CouponMember cm WHERE cm.couponMemberId.memberId = :memberId")
	List<CouponMember> findByMemberId(@Param(value = "memberId") Integer memberId);
	
	@Query("SELECT cm FROM CouponMember cm WHERE cm.couponMemberId.memberId= :memberId AND cm.couponMemberId.couponId= :couponId")
	CouponMember findByMemberIdAndCouponId(@Param(value = "memberId")Integer memberId, @Param(value = "couponId") Integer couponId);
}
