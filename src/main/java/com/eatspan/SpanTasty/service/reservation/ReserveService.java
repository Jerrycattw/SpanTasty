package com.eatspan.SpanTasty.service.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.repository.reservation.ReserveRepository;

@Service
public class ReserveService {
	
	@Autowired
	private ReserveRepository reserveRepository;
	
	// 新增訂位
	public Reserve addReserve(Reserve reserve) {
		return reserveRepository.save(reserve);
	}
	
	
	// 刪除訂位
	public void deleteReserve(Integer reserveId) {
		reserveRepository.deleteById(reserveId);
	}
	
	
	// 更新訂位
	public Reserve updateReserve(Reserve reserve) {
		Optional<Reserve> optional = reserveRepository.findById(reserve.getReserveId());
		if(optional.isPresent()) {
			return reserveRepository.save(reserve);
		}
		return null;
	}
	
	
	// 查詢訂位byId
	public Reserve findRestaurantById(Integer reserveId) {
		return reserveRepository.findById(reserveId).orElse(null);
	}

	
	// 查詢所有訂位
	public List<Reserve> findAllRestaurants() {
		return reserveRepository.findAll();
	}
	
	
	
	
	

}
