package com.eatspan.SpanTasty.entity.reservation;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity @Table(name = "reserve")
public class Reserve {
	
	
	@Id @Column(name = "reserve_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reserveId;
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC+8")
	@Column(name = "reserve_create_time")
	private LocalDateTime reserveCreateTime;
	
	@Column(name = "reserve_seat")
	private Integer reserveSeat;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC+8")
	@Column(name = "reserve_time")
	private LocalDateTime reserveTime;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC+8")
	@Column(name = "finished_time")
	private LocalDateTime finishedTime;
	
	@Column(name = "reserve_status")
	private Integer reserveStatus;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_type_id", insertable = false, updatable = false)
	private TableType tableType;
	
	
	@ManyToOne //預設(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; // 與 Restaurant 的關聯
	
//	@JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
//	  @ManyToOne //預設(fetch = FetchType.EAGER)
//    @JoinColumn(name = "member_id", insertable = false, updatable = false)
//    private Member member; // 與 Restaurant 的關聯	
	
	
	
	@PrePersist //當物件轉換成persist時先做該方法
	public void onCreate() {
		if(reserveCreateTime == null) {
			reserveCreateTime = LocalDateTime.now(); // 使用 LocalDateTime.now() 設置當前時間
		}
	}
	
	

}