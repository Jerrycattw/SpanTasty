package com.eatspan.SpanTasty.service.order;

import java.util.List;

import com.eatspan.SpanTasty.entity.order.TogoEntity;

public interface TogoService {
	
	//後臺管理查詢
	public List<TogoEntity> getAllTogo();
	public TogoEntity getTogoById(Integer togoId); // 前後共用
	public List<TogoEntity> getTogoByMemberId(Integer memberId);
	public List<TogoEntity> getTogoByPhone(String tgPhone);
	//前台顧客查詢
	public List<TogoEntity> getTogoByTgNameAndTgPhone(String tgName, String thPhone);
	
	//新增
	public TogoEntity addTogo(TogoEntity newtogo);
	//更新
	public TogoEntity updateTogoById(Integer togoId, TogoEntity updateTogo);
	//刪除=修改狀態為3
	public TogoEntity deleteTogoById(Integer togoId);
	
	
	
	
}


