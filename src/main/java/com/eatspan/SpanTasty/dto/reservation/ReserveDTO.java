package com.eatspan.SpanTasty.dto.reservation;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

//專門用來接收ajax傳來的json物件
@Data
@AllArgsConstructor
public class ReserveDTO {
	
	private Integer reserveId;
	private Integer memberId;
	private Integer restaurantId;
	private Integer reserveSeat;
	private LocalDateTime reserveTime;
	
}
