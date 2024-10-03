package com.eatspan.SpanTasty.service.order;

import java.util.List;

import com.eatspan.SpanTasty.entity.order.TogoItemEntity;
import com.eatspan.SpanTasty.entity.order.TogoItemId;

public interface TogoItemService {
	
	//查詢
	public List<TogoItemEntity> getAllTogoItemByTogoId(Integer togoId);
	public TogoItemEntity getTogoItemByTogoIdFoodId(TogoItemId togoItemId);
	//新增
	public List<TogoItemEntity> addTogoItems(Integer togoId, List<TogoItemEntity> newTogoItems);
	//更新:只能更新數量
	public TogoItemEntity updateTogoItemById(TogoItemId togoItemId, Integer amount);
	//刪除
	public void deleteTogoItemById(TogoItemId togoItemId);
	
}
