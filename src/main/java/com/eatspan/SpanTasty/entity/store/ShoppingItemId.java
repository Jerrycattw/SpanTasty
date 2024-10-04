package com.eatspan.SpanTasty.entity.store;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class ShoppingItemId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "shopping_id")
    private Integer shoppingId;
    
	@Column(name = "product_id")
    private Integer productId;
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingItemId)) return false;
        ShoppingItemId that = (ShoppingItemId) o;
        return Objects.equals(shoppingId, that.shoppingId) && 
               Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingId, productId);
    }

	public ShoppingItemId(Integer shoppingId, Integer productId) {
		this.shoppingId = shoppingId;
		this.productId = productId;
	}
}
