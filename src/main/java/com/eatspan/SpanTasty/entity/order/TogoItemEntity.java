package com.eatspan.SpanTasty.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "togo_item")
public class TogoItemEntity {
	
	@EmbeddedId
	private TogoItemId togoItemId;
	
	@Column(name = "amount")
	private Integer amount;
	
	@Column(name = "togo_item_price")
	private Integer togoItemPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_id", insertable = false, updatable = false)
	private MenuEntity menu;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "togo_id", insertable = false, updatable = false)
	private TogoEntity togo;
	
}
