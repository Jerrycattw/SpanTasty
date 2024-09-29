package com.eatspan.SpanTasty.dto.order;

import com.eatspan.SpanTasty.entity.order.MenuEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MenuDTO {
	private Integer foodId;
    private String foodName;
    private String foodKindName; //from food_kind
    private String foodPicture;
    private Integer foodPrice;
    private Integer foodStock;
    private String foodDescription;
    private Integer foodStatus;
    
    public void menuToMenuDto(MenuEntity menu) {
    	foodId = menu.getFoodId();
    	foodName = menu.getFoodName();
        foodKindName = menu.getFoodKind().getFoodKindName();
        foodPicture = menu.getFoodPicture();
        foodPrice = menu.getFoodPrice();
        foodStock = menu.getFoodStock();
        foodDescription = menu.getFoodDescription();
        foodStatus = menu.getFoodStatus();
    }
    
}
