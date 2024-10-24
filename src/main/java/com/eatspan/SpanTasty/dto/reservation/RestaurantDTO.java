package com.eatspan.SpanTasty.dto.reservation;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
	
	private Integer restaurantId;
	private String restaurantName;
	private String restaurantAddress;
	
}
