package com.eatspan.SpanTasty.entity.reservation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity @Table(name = "table_type")
public class TableType {
	
	@Id @Column(name = "table_type_id")
	private String tableTypeId;
	
	@Column(name = "table_type_name")
	private String tableTypeName;
	
	
	// mapped=> RestaurantTable 下 private TableType tableType;
	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tableType", cascade = CascadeType.ALL)
	private List<RestaurantTable> restaurantTables = new ArrayList<RestaurantTable>();
	

}
