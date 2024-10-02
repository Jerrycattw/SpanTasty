package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "coupon_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer couponId;

	@Column(name = "coupon_code")
	private String couponCode;

	@Column(name = "coupon_description")
	private String couponDescription;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "coupon_start_date")
	private LocalDate couponStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "coupon_end_date")
	private LocalDate couponEndDate;

	@Column(name = "max_coupon")
	private int maxCoupon;

	@Column(name = "per_max_coupon")
	private int perMaxCoupon;

	@Column(name = "coupon_status")
	private String couponStatus;

	@Column(name = "rules_description")
	private String rulesDescription;

	@Column(name = "discount_type")
	private String discountType;

	@Column(name = "discount")
	private int discount;

	@Column(name = "min_order_amount")
	private int minOrderDiscount;

	@Column(name = "max_discount")
	private int maxDiscount;

	@Transient
	private int receivedAmount;
}