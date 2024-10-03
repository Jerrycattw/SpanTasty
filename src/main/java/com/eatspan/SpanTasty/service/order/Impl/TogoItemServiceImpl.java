package com.eatspan.SpanTasty.service.order.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.order.TogoItemEntity;
import com.eatspan.SpanTasty.entity.order.TogoItemId;
import com.eatspan.SpanTasty.repository.order.TogoItemRepository;
import com.eatspan.SpanTasty.service.order.TogoItemService;

@Service
public class TogoItemServiceImpl implements TogoItemService {
	
	@Autowired
	private TogoItemRepository togoItemRepository;
	
	@Override
	public List<TogoItemEntity> getAllTogoItemByTogoId(Integer togoId) {
		return null;
	}

	@Override
	public TogoItemEntity getTogoItemByTogoIdFoodId(TogoItemId togoItemId) {
		return togoItemRepository.findById(togoItemId).orElse(null);
	}

	@Override
	public List<TogoItemEntity> addTogoItems(Integer togoId, List<TogoItemEntity> newTogoItems) {
		for (TogoItemEntity item : newTogoItems) {
			item.getTogoItemId().setTogoId(togoId);
		}
		return togoItemRepository.saveAll(newTogoItems);
	}
	
	@Transactional
	@Override
	public TogoItemEntity updateTogoItem(TogoItemId togoItemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTogoItem(TogoItemId togoItemId) {
		togoItemRepository.deleteById(togoItemId);
	}

}
