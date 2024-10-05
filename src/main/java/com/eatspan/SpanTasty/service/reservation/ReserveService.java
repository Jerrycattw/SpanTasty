package com.eatspan.SpanTasty.service.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.dto.reservation.ReserveCheckDTO;
import com.eatspan.SpanTasty.dto.reservation.TimeSlotDTO;
import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.repository.reservation.ReserveRepository;
import com.eatspan.SpanTasty.repository.reservation.RestaurantRepository;
import com.eatspan.SpanTasty.repository.reservation.TableTypeRepository;

@Service
public class ReserveService {
	
	@Autowired
	private ReserveRepository reserveRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private TableTypeRepository tableTypeRepository;
	
	// 新增訂位
	public Reserve addReserve(Reserve reserve) {
		
		reserve.setFinishedTime(reserve.getReserveTime().plusMinutes(reserve.getRestaurant().getEattime()));
		
		return reserveRepository.save(reserve);
	}
	
	
	// 刪除訂位
	public void deleteReserve(Integer reserveId) {
		reserveRepository.deleteById(reserveId);
	}
	
	
	// 更新訂位
	@Transactional
	public Reserve updateReserve(Reserve reserve) {
		Optional<Reserve> optional = reserveRepository.findById(reserve.getReserveId());
		if(optional.isPresent()) {
			reserve.setFinishedTime(reserve.getReserveTime().plusMinutes(reserve.getRestaurant().getEattime()));
			return reserveRepository.save(reserve);
		}
		return null;
	}
	
	
	// 查詢訂位byId
	public Reserve findReserveById(Integer reserveId) {
		return reserveRepository.findById(reserveId).orElse(null);
	}

	
	// 查詢所有訂位
	public List<Reserve> findAllReserves() {
		return reserveRepository.findAll();
	}
	
	
	// 查詢訂位by可變條件
	public List<Reserve> findReserveByCriteria(String memberName, String phone, Integer restaurantId, String tableTypeId, LocalDateTime reserveTimeStart, LocalDateTime reserveTimeEnd){
		return reserveRepository.findReserveByCriteria(memberName, phone, restaurantId, tableTypeId, reserveTimeStart, reserveTimeEnd);
	}
	
	
	
	// 查詢餐廳該天所有時段特定桌位種類的預約狀況
    public List<ReserveCheckDTO> getReserveCheck(Integer restaurantId, String tableTypeId, LocalDate checkDate) {
    	
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID"));

        // 根據餐廳的營業時間與每個時間段的用餐限制，計算出時間段
        List<TimeSlotDTO> timeSlots = generateTimeSlots(restaurant);

        List<ReserveCheckDTO> reserveChecks = new ArrayList<>();

        for (TimeSlotDTO timeSlot : timeSlots) {
            // 使用自定義的查詢方法，查詢每個時間段的預訂數量與總桌數
            Integer reservedTableCount = reserveRepository.countReservationsInTimeSlot(restaurantId, tableTypeId, checkDate, timeSlot.getSlotStar(), timeSlot.getSlotEnd());
            Integer totalTableCount = reserveRepository.countAvailableTables(restaurantId, tableTypeId);
            ReserveCheckDTO bean = new ReserveCheckDTO(timeSlot.getSlotStar(), timeSlot.getSlotEnd(), totalTableCount, reservedTableCount);
            reserveChecks.add(bean);
        }

        return reserveChecks;
    }

    
    
    // 計算餐廳的所有可用時段
    private List<TimeSlotDTO> generateTimeSlots(Restaurant restaurant) {
        List<TimeSlotDTO> timeSlots = new ArrayList<>();
        LocalTime slotStart = restaurant.getRestaurantOpentime();
        LocalTime slotEnd;

        while (slotStart.isBefore(restaurant.getRestaurantClosetime())) {
            slotEnd = slotStart.plusMinutes(restaurant.getEattime());
            timeSlots.add(new TimeSlotDTO(slotStart, slotEnd));
            slotStart = slotStart.plusMinutes(30);  // 每30分鐘一個時間段
        }

        return timeSlots;
    }
    
    
    
    // 依照預定人數查詢訂位的桌位種類ID
    public String getTableTypeIdByReserveSeat(Integer reserveSeat) {
		
    	List<TableType> tableTypes = tableTypeRepository.findAll();
    	
    	for(TableType tableType : tableTypes) {
    		if(reserveSeat == tableType.getTableTypeName()) {
    			return tableType.getTableTypeId();
    		}
    	}
    	
    	for(TableType tableType : tableTypes) {
    		if(reserveSeat+1 == tableType.getTableTypeName()) {
    			return tableType.getTableTypeId();
    		}
    	}
    	
    	return null;
	}
    
    
    
    
	
	

}
