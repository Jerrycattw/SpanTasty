package com.eatspan.SpanTasty.entity.reservation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
	
	
	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<RestaurantTable> restaurantTables = new ArrayList<RestaurantTable>();
	
	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
	private List<Reserve> reserves = new ArrayList<Reserve>();

}
