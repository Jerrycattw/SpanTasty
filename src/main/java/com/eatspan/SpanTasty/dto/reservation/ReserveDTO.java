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
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDTO {
	
	private Integer reserveId;
	private Integer memberId;
	private Integer restaurantId;
	private Integer reserveSeat;
	private LocalDate checkDate;
	private LocalTime startTime;
	private Integer tableId;
	private String reserveNote;
	
	
}
