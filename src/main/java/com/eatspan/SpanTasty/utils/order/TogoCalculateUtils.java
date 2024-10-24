package com.eatspan.SpanTasty.utils.order;

import java.util.List;

import org.springframework.stereotype.Component;

import com.eatspan.SpanTasty.entity.order.TogoItemEntity;

@Component
public class TogoCalculateUtils {
    
    public Integer sumOfTotalPrice(List<TogoItemEntity> togoItemList) {
        int newTotalPrice = 0;
        
        // 計算總價
        for (TogoItemEntity item : togoItemList) {
        	newTotalPrice = newTotalPrice+ item.getTogoItemPrice();	
        }
		
        return newTotalPrice;
    }
}
