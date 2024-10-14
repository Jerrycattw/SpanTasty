package com.eatspan.SpanTasty.entity.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

//代表購物車中的一個商品項目。
//包含了具體商品的詳細信息以及該商品的數量。

@Getter
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;


}
