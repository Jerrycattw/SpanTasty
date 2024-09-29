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
	
    public RestaurantTableId(Integer restaurantId, String tableTypeId) {
        this.restaurantId = restaurantId;
        this.tableTypeId = tableTypeId;
    }
	
    
    // hashCode 和 equals 方法（用於確保複合鍵的唯一性）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTableId that = (RestaurantTableId) o;
        return Objects.equals(restaurantId, that.restaurantId) && 
               Objects.equals(tableTypeId, that.tableTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, tableTypeId);
    }
    
	
}
