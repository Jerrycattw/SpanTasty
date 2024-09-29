package com.eatspan.SpanTasty.entity.rental;

import java.io.Serializable;

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

@Entity
@Table(name = "tableware_stock")
@IdClass(TablewareStock.TablewareStockId.class)
@NoArgsConstructor
@Getter
@Setter
public class TablewareStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id@Column(name = "tableware_id")
	private Integer tablewareId;
	@Id@Column(name = "restaurant_id")
	private Integer restaurantId;
	@Column(name = "stock")
	private Integer stock;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tableware_id", insertable = false, updatable = false)
	private Tableware tableware;
	
	@Data
	public static class TablewareStockId implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int tablewareId;
        private String restaurantId;
	}
}
