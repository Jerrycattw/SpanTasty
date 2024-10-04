package com.eatspan.SpanTasty.dto.reservation;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ReserveCheckDTO {
	
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;
	
	private Integer totalTableNumber;
	
	private Integer reservedTableNumber;
	
	
}
