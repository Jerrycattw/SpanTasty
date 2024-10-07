package com.eatspan.SpanTasty.utils.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eatspan.SpanTasty.entity.order.TogoItemEntity;
import com.eatspan.SpanTasty.service.order.TogoItemService;

@Component
public class TogoCalculateUtils {

    @Autowired
    private TogoItemService togoItemService; 

    public Integer sumOfTotalPrice(Integer togoId) {
        // 使用 togoId 獲取所有 TogoItemEntity
        List<TogoItemEntity> togoItemList = togoItemService.getAllTogoItemByTogoId(togoId);
        int newTotalPrice = 0;
        
        // 計算總價
        for (TogoItemEntity item : togoItemList) {
        	newTotalPrice = newTotalPrice+ item.getTogoItemPrice();	
        }
		
        return newTotalPrice;
    }
}
