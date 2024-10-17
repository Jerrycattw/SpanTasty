package com.eatspan.SpanTasty.dto.rental;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CartRequestDTO {
	
	@JsonProperty("tablewareId")
	private Integer tablewareId;
	
	@JsonProperty("rentItemQuantity")
	private Integer rentItemQuantity;

}
