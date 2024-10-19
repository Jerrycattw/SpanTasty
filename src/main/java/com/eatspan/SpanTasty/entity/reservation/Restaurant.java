package com.eatspan.SpanTasty.entity.reservation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.eatspan.SpanTasty.entity.order.TogoEntity;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant {
	
	
	@Id @Column(name = "restaurant_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer restaurantId;
	
	@Column(name = "restaurant_name")
	private String restaurantName;
	
	@Column(name = "restaurant_address")
	private String restaurantAddress;
	
	@Column(name = "restaurant_phone")
	private String restaurantPhone;
	
	
	//@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "UTC+8")
	@Column(name = "restaurant_opentime")
	private LocalTime restaurantOpentime;
	
	//@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm", timezone = "UTC+8")
	@Column(name = "restaurant_closetime")
	private LocalTime restaurantClosetime;
	
	
	@Column(name = "eattime")
	private Integer eattime;
	
	@Column(name = "restaurant_status")
	private Integer restaurantStatus;
	
	@Column(name = "restaurant_img")
	private String restaurantImg;
	
	@Column(name = "restaurant_desc")
	private String restaurantDesc;
	
	@Column(name = "reserve_percent")
	private Integer reservePercent;
	
	@Column(name = "reserve_time_scale")
	private Integer reserveTimeScale;
	
	@Column(name = "reserve_min")
	private Integer reserveMin;
	
	@Column(name = "reserve_max")
	private Integer reserveMax;
	
	
	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<RestaurantTable> restaurantTables = new ArrayList<RestaurantTable>();
	
	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<Reserve> reserves = new ArrayList<Reserve>();

	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL) // 'restaurant' 是 `TablewareStock` 中的字段名
    private List<TablewareStock> stocks = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<TogoEntity> togo = new ArrayList<TogoEntity>();
	
	
	@PrePersist //當物件轉換成persist時先做該方法
	public void onCreate() {
		
		if(this.restaurantStatus == null) {
			this.restaurantStatus = 3; // 餐廳狀態
		}
		if(this.reservePercent == null) {
			this.reservePercent = 100; // 餐廳開放訂位的比例
		}
		if(this.reserveTimeScale == null) {
			this.reserveTimeScale = 30; // 訂位的區間(預設為30分鐘)
		}
		if(this.reserveMin == null) {
			this.reserveMin = 2; // 訂位的區間(預設為30分鐘)
		}
		if(this.reserveMax == null) {
			this.reserveMax = 10; // 訂位的區間(預設為30分鐘)
		}
	}
	
}
