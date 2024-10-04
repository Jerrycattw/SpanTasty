package com.eatspan.SpanTasty.dto.discount;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CouponDTO {
	
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer couponId;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String couponCode;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String couponDescription;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate couponStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate couponEndDate;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer maxCoupon;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer perMaxCoupon;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String couponStatus;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String rulesDescription;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String discountType;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer discount;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer minOrderDiscount;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer maxDiscount;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private List<TagDTO> tags;
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private Integer receivedAmount;
	
	public CouponDTO(Integer couponId, String couponCode, String couponDescription, LocalDate couponStartDate,
			LocalDate couponEndDate, Integer maxCoupon, Integer perMaxCoupon, String couponStatus, String rulesDescription,
			String discountType, Integer discount, Integer minOrderDiscount, Integer maxDiscount, List<TagDTO> tags,
			Integer receivedAmount) {
		this.couponId = couponId;
		this.couponCode = couponCode;
		this.couponDescription = couponDescription;
		this.couponStartDate = couponStartDate;
		this.couponEndDate = couponEndDate;
		this.maxCoupon = maxCoupon;
		this.perMaxCoupon = perMaxCoupon;
		this.couponStatus = couponStatus;
		this.rulesDescription = rulesDescription;
		this.discountType = discountType;
		this.discount = discount;
		this.minOrderDiscount = minOrderDiscount;
		this.maxDiscount = maxDiscount;
		this.tags = tags;
		this.receivedAmount = receivedAmount;
	}
}
