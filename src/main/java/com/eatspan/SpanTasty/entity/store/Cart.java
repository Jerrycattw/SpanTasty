package com.eatspan.SpanTasty.entity.store;

import java.util.ArrayList;
import java.util.List;

//代表整個購物車包含了多個商品項目（CartItem），
//提供了與購物車相關的操作，例如添加商品、刪除商品和計算總價格。

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        CartItem cartItem = new CartItem(product, quantity);
        items.add(cartItem);
    }

    public void removeItem(Integer productId) {
        items.removeIf(item -> item.getProduct().getProductId().equals(productId));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getTotalPrice() {
        return items.stream()
                .mapToInt(item -> item.getProduct().getProductPrice() * item.getQuantity())
                .sum();
    }
}
