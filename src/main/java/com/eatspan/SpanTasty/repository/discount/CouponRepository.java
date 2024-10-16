package com.eatspan.SpanTasty.repository.discount;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.discount.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,Integer> {
	
	Coupon findByCouponId(Integer couponId);
	
	Coupon findByCouponCode(String couponCode);
	
	@Query("SELECT c from Coupon c WHERE c.couponId = :couponId "
			+ "OR c.couponCode LIKE %:keyWord% OR c.couponDescription LIKE %:keyWord% "
			+ "OR c.couponStatus LIKE %:keyWord% OR c.rulesDescription LIKE %:keyWord% "
			+ "OR c.discountType LIKE %:keyWord% OR c.couponStartDate = :dateKeyWord OR c.couponEndDate = :dateKeyWord ")
	List<Coupon> searchCoupons(@Param(value="couponId") Integer intKeyWord,@Param(value="keyWord") String keyWord,@Param(value="dateKeyWord") LocalDate date);

}
