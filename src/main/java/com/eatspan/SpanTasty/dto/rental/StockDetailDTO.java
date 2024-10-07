package com.eatspan.SpanTasty.dto.rental;

import lombok.Data;

@Data
public class StockDetailDTO {

	private Integer tablewareId;
	
    private String tablewareName;
    
    private Integer restaurantId;
    
    private String restaurantName;
    
    private int stock;

}
