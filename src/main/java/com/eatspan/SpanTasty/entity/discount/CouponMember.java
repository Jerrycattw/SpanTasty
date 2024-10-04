package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
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
public class CouponMember implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private CouponMemberId couponMemberId;
	
	@Column(name = "total_amount")
	private int totalAmount;
	
	@Column(name = "usage_amount")
	private int usageAmount;
	
	
	public CouponMember(CouponMemberId couponMemberId, int totalAmount, int usageAmount) {
		this.couponMemberId = couponMemberId;
		this.totalAmount = totalAmount;
		this.usageAmount = usageAmount;
	}


	@ManyToOne
	@JoinColumn(name = "coupon_id", insertable = false, updatable = false)
	private Coupon coupon;
	
	
	
	public void incrementAmounts() {
        this.totalAmount++;
        this.usageAmount++;
    }
}