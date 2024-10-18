package com.eatspan.SpanTasty.dto.store;

import java.util.List;

import com.eatspan.SpanTasty.entity.store.ShoppingItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingItemDTO {
	
	private List<ShoppingItem> shoppingItems;
	private Integer totalAmount;
	private Integer memberId;
}
