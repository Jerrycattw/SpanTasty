package com.eatspan.SpanTasty.entity.store;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingItemId implements Serializable {
    private Integer shoppingId;
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
}
