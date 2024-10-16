package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eatspan.SpanTasty.entity.account.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
	private Integer maxCoupon;

	@Column(name = "per_max_coupon")
	private Integer perMaxCoupon;

	@Column(name = "coupon_status")
	private String couponStatus;

	@Column(name = "rules_description")
	private String rulesDescription;

	@Column(name = "discount_type")
	private String discountType;

	@Column(name = "discount")
	private Integer discount;

	@Column(name = "min_order_amount")
	private Integer minOrderDiscount;

	@Column(name = "max_discount")
	private Integer maxDiscount;

	@Transient
	private Integer receivedAmount;
	
	@OneToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL,mappedBy = "coupon",orphanRemoval=true)
	private List<Tag> tags=new ArrayList<Tag>();	
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon")
	private Set<CouponMember> couponMember = new HashSet<CouponMember>();
}