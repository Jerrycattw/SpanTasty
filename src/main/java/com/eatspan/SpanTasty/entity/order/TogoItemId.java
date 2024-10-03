package com.eatspan.SpanTasty.entity.order;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class TogoItemId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "togo_id")
	private Integer togoId;
	
	@Column(name = "food_id")
	private Integer foodId;

	public TogoItemId(Integer togoId, Integer foodId) {
		this.togoId = togoId;
		this.foodId = foodId;
	}
	
	
}
