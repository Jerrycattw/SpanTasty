package com.eatspan.SpanTasty.entity.reservation;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class RestaurantTableId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "restaurant_id")
	private Integer restaurantId;
	
	@Column(name = "table_type_id")
	private String tableTypeId;
	
	@Column(name = "table_id")
	private Integer tableId;

	public RestaurantTableId(Integer restaurantId, String tableTypeId, Integer tableId) {
		super();
		this.restaurantId = restaurantId;
		this.tableTypeId = tableTypeId;
		this.tableId = tableId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(restaurantId, tableId, tableTypeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RestaurantTableId other = (RestaurantTableId) obj;
		return Objects.equals(restaurantId, other.restaurantId) && Objects.equals(tableId, other.tableId)
				&& Objects.equals(tableTypeId, other.tableTypeId);
	}
	

	
}
