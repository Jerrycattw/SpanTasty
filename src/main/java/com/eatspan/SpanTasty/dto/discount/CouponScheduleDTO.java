package com.eatspan.SpanTasty.dto.discount;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponScheduleDTO {
	
	private Integer scheduleId;
	private String scheduleName;
	private Integer couponId;
	private Integer memberId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "UTC+8")
	private LocalDateTime scheduleTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "UTC+8")
	private LocalDateTime createTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "UTC+8")
	private LocalDateTime processTime;
	
	private String status;
	
}
