package com.eatspan.SpanTasty.service.rental;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.repository.rental.RentRepository;

@Service
public class RentService {
	
	@Autowired
	private RentRepository rentRepository;
	
	
	//新增訂單
	public Rent addRent(Rent rent) {
		return rentRepository.save(rent);
	}
	
	
	//刪除訂單
	public void deleteRent(Integer rentId) {
		rentRepository.deleteById(rentId);
	}
	
	
	//更新訂單
	@Transactional
	public Rent updateRent(Rent rent) {
		Optional<Rent> optional = rentRepository.findById(rent.getRentId());
		if(optional.isPresent()) {
			return rentRepository.save(rent);
		}
		return null;
	}
	
	
	//更新訂單
	@Transactional
	public Rent updateRent(Integer rentId, Integer rentDeposit, Date rentDate, Integer restaurantId, Integer memberId, Date dueDate, Date returnDate, Integer rentStatus, String rentMemo, Integer returnRestaurantId) {
		Optional<Rent> optional = rentRepository.findById(rentId);
		if(optional.isPresent()) {
			Rent rent = optional.get();
			rent.setRentDeposit(rentDeposit);
			rent.setRentDate(rentDate);
			rent.setRestaurantId(restaurantId);
			rent.setMemberId(memberId);
			rent.setDueDate(dueDate);
			rent.setReturnDate(returnDate);
			rent.setRentStatus(rentStatus);
			rent.setRentMemo(rentMemo);
			rent.setReturnRestaurantId(returnRestaurantId);
			return rent;
		}
		return null;
	}
	
	
	//查詢訂單(By條件)
	public List<Rent> findRentsByCriteria(Integer rentId, Integer memberId, Integer restaurantId, Integer rentStatus, Date rentDateStart, Date rentDateEnd){
		return rentRepository.findByCriteria(rentId, memberId, restaurantId, rentStatus, rentDateStart, rentDateEnd);
	}
	
	
	//查詢訂單(By過期)
	public List<Rent> findOvertimeRents(){
		return rentRepository.findByOverDueDate();
	}
	
	
	//查詢訂單(By訂單編號)
	public Rent findRentById(Integer rentId) {
		Optional<Rent> optional = rentRepository.findById(rentId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	//查詢所有訂單
	public List<Rent> findAllRents() {
		return rentRepository.findAll();
	}

	
	//查詢所有訂單(Page)
	public Page<Rent> findAllRentPages(Integer page){
		Pageable pageable = PageRequest.of(page-1, 10, Sort.Direction.ASC, "rentId");
		return rentRepository.findAll(pageable);
	}
}
