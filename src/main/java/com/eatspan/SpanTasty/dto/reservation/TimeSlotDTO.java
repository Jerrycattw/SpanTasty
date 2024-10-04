package com.eatspan.SpanTasty.dto.reservation;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimeSlotDTO {
	
	private LocalTime slotStar;
	private LocalTime slotEnd;

}
