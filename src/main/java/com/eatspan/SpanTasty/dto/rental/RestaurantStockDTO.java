package com.eatspan.SpanTasty.dto.rental;


import java.util.List;

import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;

import lombok.Data;
@Data
public class RestaurantStockDTO {

	private Restaurant restaurant;
	
    private List<TablewareStock> stocks;

}
