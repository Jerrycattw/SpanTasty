package com.eatspan.SpanTasty.dto.rental;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
public class RentDetailDTO {
	
	private Integer rentId;
	
	private Integer rentDeposit;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date rentDate;
	
	private Integer restaurantId;
	
	private String restaurantName;
	
	private Integer memberId;
	
	private String memberName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date returnDate;
	
	private Integer rentStatus;
	
	private String rentMemo;
	
	private Integer returnRestaurantId;
	
	private String returnRestaurantName;

}
