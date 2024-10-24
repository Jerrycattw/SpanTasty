package com.eatspan.SpanTasty.dto.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//專門用來接收ajax傳來的json物件
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportReserveDTO {
	
	
	private Integer reserveId;
	
	private String memberName;
	
	private String memberPhone;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthday;
	
	private String restaurantName;
	
	private String restaurantPhone;
	
	private String restaurantAddress;
	
	private Integer reserveSeat;
	
	private String reserveTime;
	
	private String tableTypeId;
	
	private String reserveNote;
	
	
	
	public ExportReserveDTO(Reserve reserve) {
		this.reserveId = reserve.getReserveId();
		this.memberName = reserve.getMember().getMemberName();
		this.memberPhone = reserve.getMember().getPhone();
		this.birthday = reserve.getMember().getBirthday();
		this.restaurantName = reserve.getRestaurant().getRestaurantName();
		this.restaurantPhone = reserve.getRestaurant().getRestaurantPhone();
		this.restaurantAddress = reserve.getRestaurant().getRestaurantAddress();
		this.reserveSeat = reserve.getReserveSeat();
		this.reserveTime = reserve.getReserveTime().toString();
		this.tableTypeId = reserve.getTableType().getTableTypeName().toString()+"人桌";
		this.reserveNote = reserve.getReserveNote();
	}
	
	
	
	
}
