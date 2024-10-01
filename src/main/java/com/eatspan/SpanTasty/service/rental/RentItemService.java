package com.eatspan.SpanTasty.service.rental;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.repository.rental.RentItemRepository;

@Service
public class RentItemService {
	
	@Autowired
	private RentItemRepository rentItemRepository;
	
	
	//新增訂單明細
	public RentItem addRentItem(RentItem rentItem) {
		return rentItemRepository.save(rentItem);
	}
	
	
	//刪除訂單明細(By訂單編號(多筆))
	public void deleteRentItems(Integer rentId) {
		rentItemRepository.deleteByRentId(rentId);
	}

	
	//更改訂單明細
	@Transactional
	public RentItem updateRentItem(RentItem rentItem) {
		RentItem setRentItem = rentItemRepository.findById(rentItem.getRentId(), rentItem.getTablewareId());
		if(setRentItem != null) {
			return rentItemRepository.save(setRentItem);
		}
		return null;
	}
	
	
	//更改訂單明細
	@Transactional
	public RentItem updateRentItem(Integer rentId, Integer tablewareId, Integer rentItemQuantity, Integer rentItemDeposit, String returnMemo, Integer returnStatus) {
		RentItem rentItem = rentItemRepository.findById(rentId, tablewareId);
		if(rentItem != null) {
			rentItem.setRentItemQuantity(rentItemQuantity);
			rentItem.setRentItemDeposit(rentItemDeposit);
			rentItem.setReturnMemo(returnMemo);
			rentItem.setReturnStatus(returnStatus);
			return rentItem;
		}
		return null;
	}
	
	
	//歸還訂單明細
	@Transactional
	public RentItem returnRentItem(Integer rentId, Integer tablewareId, String returnMemo, Integer returnStatus) {
		RentItem rentItem = rentItemRepository.findById(rentId, tablewareId);
		if(rentItem != null) {
			rentItem.setReturnMemo(returnMemo);
			rentItem.setReturnStatus(returnStatus);
			return rentItem;
		}
		return null;
	}
	
	
	//查詢訂單明細(By訂單 餐具編號)
	public RentItem findRentItemById(Integer rentId, Integer tablewareId) {
		RentItem rentItem = rentItemRepository.findById(rentId, tablewareId);
		if (rentItem != null) {
			return rentItem;
		}
		return null;
	}
	
	
	//查詢訂單明細(By訂單編號)
	public List<RentItem> findRentItemsByRentId(Integer rentId) {
		return rentItemRepository.findByRentId(rentId);
	}
	
	
	//查詢全訂單明細
	public List<RentItem> findAllRentItems(){
		return rentItemRepository.findAll();
	}
	
	
	
	
}
