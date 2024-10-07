package com.eatspan.SpanTasty.dto.rental;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StockKeywordDTO {
	
	@JsonProperty("tablewareId")
	private Integer tablewareId;

	@JsonProperty("restaurantId")
	private Integer restaurantId;
}
