package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "point_set")
public class PointSet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "point_set")
	private String pointSetName;
	
	@Column(name = "amount_per_point")
	private int amountPerPoint;
	
	@Column(name = "points_earned")
	private int pointsEarned;
	
	@Column(name = "point_ratio")
	private int pointRatio;
	
	@Column(name = "expiry_month")
	private String expiryMonth;
	
	@Column(name = "expiry_day")
	private String expiryDay;
	
	@Column(name = "birth_type")
	private String birthType;
	
	@Column(name = "set_description")
	private String setDescription;
	
	@Column(name = "is_expiry")
	private String isExpiry;
}