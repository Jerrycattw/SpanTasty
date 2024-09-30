package com.eatspan.SpanTasty.service.rental;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.rental.Rent;
import com.eatspan.SpanTasty.repository.rental.RentRepository;

@Service
public class RentService {
	@Autowired
	private RentRepository rentRepository;

	public Rent saveRent(Rent rent) {
		return rentRepository.save(rent);
	}
	
	public List<Rent> findAllRents() {
		return rentRepository.findAll();
	}
	
	public Rent findRentById(Integer rentId) {
		Optional<Rent> optional = rentRepository.findById(rentId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public void deleteRentById(Integer rentId) {
		rentRepository.deleteById(rentId);
	}
	
	@Transactional
	public Rent updateRentById(Integer rentId, Integer rentDeposit, Date rentDate, Integer restaurantId, Integer memberId, Date dueDate, Date returnDate, Integer rentStatus, String rentMemo, Integer returnRestaurantId) {
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
	
	public List<Rent> findRentsByCriteria(Integer rentId, Integer memberId, Integer restaurantId, Integer rentStatus, Date rentDateStart, Date rentDateEnd){
		return rentRepository.findRentsByCriteria(rentId, memberId, restaurantId, rentStatus, rentDateStart, rentDateEnd);
	}
	
	public List<Rent> findRentsByOvertime(){
		return rentRepository.findRentsByOverDueDate();
	}
	
	public List<Integer> findRentIds(){
		return rentRepository.findRentIds();
	}
	
}
