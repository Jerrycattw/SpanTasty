package com.eatspan.SpanTasty.entity.rental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rent")
@Getter
@Setter
@NoArgsConstructor
public class Rent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id@Column(name = "rent_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rentId;
	
	@Column(name = "rent_deposit")
	private Integer rentDeposit;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "rent_date")
	@Temporal(TemporalType.DATE)
	private Date rentDate;
	
	@Column(name = "restaurant_id")
	private Integer restaurantId;
	
	@Column(name = "member_id")
	private Integer memberId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "due_date")
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "return_date")
	@Temporal(TemporalType.DATE)
	private Date returnDate;
	
	@Column(name = "rent_status")
	private Integer rentStatus;
	
	@Column(name = "rent_memo")
	private String rentMemo;
	
	@Column(name = "return_restaurant_id")
	private Integer returnRestaurantId;
	
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rent")
	private List<RentItem> rentItems = new ArrayList<RentItem>();
}
