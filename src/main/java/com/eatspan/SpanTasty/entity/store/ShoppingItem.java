package com.eatspan.SpanTasty.entity.store;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "shopping_item")
public class ShoppingItem {

	@EmbeddedId
	private ShoppingItemId id;


	@Column(name = "shopping_item_quantity")
	private Integer shoppingItemQuantity;

	@Column(name = "shopping_item_price")
	private Integer shoppingItemPrice;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productId") // 映射主鍵的 productId避免重複列名
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	@ManyToOne
	@MapsId("shoppingId") // 映射主鍵的 shoppingId避免重複列名
	@JoinColumn(name = "shopping_id", insertable = false, updatable = false)
	private ShoppingOrder shoppingOrder;

	 // 提供對複合主鍵的 getter 方法
	public Integer getProductId() {
        return id.getProductId(); // 透過複合主鍵獲取產品 ID
    }

    public Integer getShoppingId() {
        return id.getShoppingId(); // 透過複合主鍵獲取購物 ID
    }
}
