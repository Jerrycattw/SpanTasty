package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CouponMemberId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "coupon_id")
	private Integer couponId;

	@Column(name = "member_id")
	private Integer memberId;

	public CouponMemberId(Integer couponId, Integer memberId) {
		super();
		this.couponId = couponId;
		this.memberId = memberId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		CouponMemberId couponMemberId = (CouponMemberId) o;
		return couponId.equals(couponMemberId.couponId) && memberId.equals(couponMemberId.memberId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(couponId, memberId);
	}

	
}
