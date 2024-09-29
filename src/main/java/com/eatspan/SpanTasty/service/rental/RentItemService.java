package com.eatspan.SpanTasty.service.rental;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.repository.rental.RentItemRepository;

@Service
public class RentItemService {
	@Autowired
	private RentItemRepository rentItemRepository;
	
	public RentItem saveRentItem(RentItem rentItem) {
		return rentItemRepository.save(rentItem);
	}
	
	public RentItem findRentItemByRentItemId(Integer rentId, Integer tablewareId) {
		RentItem rentItem = rentItemRepository.findRentItemByRentItemId(rentId, tablewareId);
		if (rentItem != null) {
			return rentItem;
		}
		return null;
	}
	
	public List<RentItem> findRentItemsByRentId(Integer rentId) {
		return rentItemRepository.findRentItemsByRentId(rentId);
	}
	
	public List<RentItem> findAllRentItems(){
		return rentItemRepository.findAll();
	}
	
	public void deleteRentItem(RentItem rentItem) {
		rentItemRepository.delete(rentItem);
	}
	
	public RentItem updateRentItem(Integer rentId, Integer tablewareId, Integer rentItemQuantity, Integer rentItemDeposit, String returnMemo, Integer returnStatus) {
		RentItem rentItem = rentItemRepository.findRentItemByRentItemId(rentId, tablewareId);
		if(rentItem != null) {
			rentItem.setRentItemQuantity(rentItemQuantity);
			rentItem.setRentItemDeposit(rentItemDeposit);
			rentItem.setReturnMemo(returnMemo);
			rentItem.setReturnStatus(returnStatus);
			return rentItem;
		}
		return null;
	}
	
	public RentItem returnRentItem(Integer rentId, Integer tablewareId, String returnMemo, Integer returnStatus) {
		RentItem rentItem = rentItemRepository.findRentItemByRentItemId(rentId, tablewareId);
		if(rentItem != null) {
			rentItem.setReturnMemo(returnMemo);
			rentItem.setReturnStatus(returnStatus);
			return rentItem;
		}
		return null;
	}
	
}
