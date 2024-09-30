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
@Table(name = "rent_item")
@IdClass(RentItem.RentItemId.class)
@NoArgsConstructor
@Getter
@Setter
public class RentItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id@Column(name = "rent_id")
	private int rentId;
	@Id@Column(name = "tableware_id")
	private int tablewareId;
	@Column(name = "rent_item_quantity")
	private int rentItemQuantity;
	@Column(name = "rent_item_deposit")
	private int rentItemDeposit;
	@Column(name = "return_memo")
	private String returnMemo;
	@Column(name = "return_status")
	private int returnStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_id", insertable = false, updatable = false)
    private Rent rent;

	@Data
	public static class RentItemId implements Serializable {
		private static final long serialVersionUID = 1L;
		
        private int rentId;
        private int tablewareId;
	}
}