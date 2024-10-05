package com.eatspan.SpanTasty.entity.rental;

import java.io.Serializable;


import com.eatspan.SpanTasty.entity.reservation.Restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tableware_stock")
@IdClass(TablewareStock.TablewareStockId.class)
public class TablewareStock implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id@Column(name = "tableware_id")
	private Integer tablewareId;
	
	@Id@Column(name = "restaurant_id")
	private Integer restaurantId;
	
	@Column(name = "stock")
	private Integer stock;
	
	
//	@MapsId("tablewareId")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableware_id", insertable = false, updatable = false)
	private Tableware tableware;
	
	
//	@MapsId("restaurantId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
	private Restaurant restaurant;

	
//	@EmbeddedId
//	private StockId stockId;
	
	
	@Data
	public static class TablewareStockId implements Serializable {
		
		private Integer tablewareId;
        private Integer restaurantId;
	}
}
