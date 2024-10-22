package com.eatspan.SpanTasty.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantDTO {
	private Integer restaurantId;
	private String restaurantName;
	private String restaurantAddress;
	
}
