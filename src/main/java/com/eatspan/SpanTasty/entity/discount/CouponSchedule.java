package com.eatspan.SpanTasty.entity.discount;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coupon_schedule")
public class CouponSchedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private Integer scheduleId;
	
	@Column(name = "schedule_name")
	private String scheduleName;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumns({
        @JoinColumn(name = "coupon_id", referencedColumnName = "coupon_id"),
        @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    })
	private CouponMember couponMember;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "UTC+8")
	@Column(name = "schedule_time")	
	private LocalDateTime scheduleTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "UTC+8")
	@Column(name = "create_time")
	private LocalDateTime createTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS", timezone = "UTC+8")
	@Column(name = "process_time")
	private LocalDateTime processTime;
	
	@Column(name = "status")
	private String status;
	
	
	@PrePersist
	protected void onCreate() {
	    if (createTime == null) {
	        createTime = LocalDateTime.now();
	    }
	}
}
