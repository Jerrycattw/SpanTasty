package com.eatspan.SpanTasty.service.order.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.order.MenuEntity;
import com.eatspan.SpanTasty.entity.order.TogoEntity;
import com.eatspan.SpanTasty.entity.order.TogoItemEntity;
import com.eatspan.SpanTasty.entity.order.TogoItemId;
import com.eatspan.SpanTasty.repository.order.MenuRepository;
import com.eatspan.SpanTasty.repository.order.TogoItemRepository;
import com.eatspan.SpanTasty.repository.order.TogoRepository;
import com.eatspan.SpanTasty.service.order.TogoItemService;
import com.eatspan.SpanTasty.utils.order.TogoCalculateUtils;

@Service
public class TogoItemServiceImpl implements TogoItemService {
	
	@Autowired
	private TogoItemRepository togoItemRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private TogoRepository togoRepository;
	
	@Autowired
	private TogoCalculateUtils togoCalculateUtils;
	
	@Override
	public List<TogoItemEntity> getAllTogoItemByTogoId(Integer togoId) {
		return togoItemRepository.findByTogoId(togoId);
	}

	@Override
	public TogoItemEntity getTogoItemByTogoIdFoodId(TogoItemId togoItemId) {
		return togoItemRepository.findById(togoItemId).orElse(null);
	}

	@Override
	public List<TogoItemEntity> addTogoItems(Integer togoId, List<TogoItemEntity> newTogoItems) {
		List<TogoItemEntity> savedTogoItems = new ArrayList<>();
		for (TogoItemEntity item : newTogoItems) {
			// 檢查togoItem是否存在
			Optional<TogoItemEntity> existingItemOptional = togoItemRepository.findById(item.getTogoItemId());
			if (existingItemOptional.isPresent()) {
				// foodId存在：數量相加
				TogoItemEntity existingItem = existingItemOptional.get();
	            item.setAmount(existingItem.getAmount() + item.getAmount());
	            savedTogoItems.add(item);
			} else {
				// foodId不存在：新增
				savedTogoItems.add(item);
			}
			// 獲取foodId並檢查MenuEntity是否存在
            Integer foodId = item.getTogoItemId().getFoodId();
            Optional<MenuEntity> foodOptional = menuRepository.findById(foodId);
            // 計算項目總合
            if (foodOptional.isPresent()) {
            	MenuEntity menu = foodOptional.get();
                // 設置menu
                item.setMenu(menu);
                Integer foodPrice = menu.getFoodPrice();
                Integer amount = item.getAmount();
                item.setTogoItemPrice(foodPrice*amount);
            }
		}
		List<TogoItemEntity> newTogoItemList = togoItemRepository.saveAll(savedTogoItems);
		// 計算新的總金額
		List<TogoItemEntity> togoItemList = togoItemRepository.findByTogoId(togoId);
	    Integer newTotalPrice = togoCalculateUtils.sumOfTotalPrice(togoItemList);
	    // 更新訂單的總金額
	    Optional<TogoEntity> togoOptional = togoRepository.findById(togoId);
	    if (togoOptional.isPresent()) {
	        TogoEntity togo = togoOptional.get();
	        togo.setTotalPrice(newTotalPrice);
	        togoRepository.save(togo); // 保存更新的訂單
	    }
		
		return newTogoItemList;
	}
	public Integer calculateTotalPrice(Integer togoId) {
        List<TogoItemEntity> togoItemList = getAllTogoItemByTogoId(togoId);
        return togoCalculateUtils.sumOfTotalPrice(togoItemList);
    }
	
	@Transactional
	@Override
	public TogoItemEntity updateTogoItemById(TogoItemId togoItemId, Integer amount) {
		Optional<TogoItemEntity> optional = togoItemRepository.findById(togoItemId);
		if (optional.isPresent()) {
			TogoItemEntity togoItem = optional.get();
			togoItem.setAmount(amount);
			Integer foodPrice = togoItem.getMenu().getFoodPrice();
			togoItem.setTogoItemPrice(amount*foodPrice);
			
			// 更新訂單總金額
			Integer togoId = togoItemId.getTogoId();
			List<TogoItemEntity> togoItemList = togoItemRepository.findByTogoId(togoId);
		    Integer newTotalPrice = togoCalculateUtils.sumOfTotalPrice(togoItemList);
			Optional<TogoEntity> togoOptional = togoRepository.findById(togoItemId.getTogoId());
		    if (togoOptional.isPresent()) {
		        TogoEntity togo = togoOptional.get();
		        togo.setTotalPrice(newTotalPrice);
		        togoRepository.save(togo); // 保存更新的訂單
		    }
			
			return togoItem;
		}
		return null;
	}

	@Override
	public void deleteTogoItemById(TogoItemId togoItemId) {
		togoItemRepository.deleteById(togoItemId);
		// 更新訂單總金額
		Integer togoId = togoItemId.getTogoId();
		List<TogoItemEntity> togoItemList = togoItemRepository.findByTogoId(togoId);
	    Integer newTotalPrice = togoCalculateUtils.sumOfTotalPrice(togoItemList);
		Optional<TogoEntity> togoOptional = togoRepository.findById(togoItemId.getTogoId());
	    if (togoOptional.isPresent()) {
	        TogoEntity togo = togoOptional.get();
	        togo.setTotalPrice(newTotalPrice);
	        togoRepository.save(togo); // 保存更新的訂單
	    }
		
	}
	
}
