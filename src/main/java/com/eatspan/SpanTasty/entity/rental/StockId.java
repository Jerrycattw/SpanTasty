package com.eatspan.SpanTasty.entity.rental;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

//@Setter
//@Getter
//@Embeddable
//public class StockId implements Serializable{
//	private static final long serialVersionUID = 1L;
//
//	private Integer tablewareId;
//	
//	private Integer restaurantId;
//	
//	public StockId() {
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(restaurantId, tablewareId);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		StockId other = (StockId) obj;
//		return Objects.equals(restaurantId, other.restaurantId) && Objects.equals(tablewareId, other.tablewareId);
//	}
//}
