package com.eatspan.SpanTasty.entity.order;

import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(foodId, togoId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TogoItemId other = (TogoItemId) obj;
		return Objects.equals(foodId, other.foodId) && Objects.equals(togoId, other.togoId);
	}
	
	
	
}
