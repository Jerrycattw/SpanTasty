package com.eatspan.SpanTasty.entity.discount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity @Table(name = "member_coupon")
public class CouponMember {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 虚拟主键
	
	@Column(name = "coupon_id")
	private int couponId;
	
	@Column(name = "member_id")
	private int memberId;
	
	@Column(name = "total_amount")
	private int totalAmount;
	
	@Column(name = "usage_amount")
	private int usageAmount;
	
	@ManyToOne
	@JoinColumn(name = "coupon_id", insertable = false, updatable = false)
	private Coupon coupon;
}