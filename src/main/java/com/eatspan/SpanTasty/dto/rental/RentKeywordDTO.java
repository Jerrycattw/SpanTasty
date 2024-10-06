package com.eatspan.SpanTasty.dto.rental;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RentKeywordDTO {
	
	@JsonProperty("rentId")
	private Integer rentId;
	
	@JsonProperty("memberId")
	private Integer memberId;
	
	@JsonProperty("restaurantId")
	private Integer restaurantId;
	
	@JsonProperty("rentStatus")
	private Integer rentStatus;
	
	@JsonProperty("rentDateStart")
	private String rentDateStart;
	
	@JsonProperty("rentDateEnd")
	private String rentDateEnd;

}
