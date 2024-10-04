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

	@ManyToOne//(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	@ManyToOne
	@JoinColumn(name = "shopping_id", insertable = false, updatable = false)
	private ShoppingOrder shoppingOrder;


	public ShoppingItem(ShoppingItemId id) {
		this.id = id;
	}

	public ShoppingItem(ShoppingItemId id, Integer shoppingItemQuantity) {
		this.id = id;
		this.shoppingItemQuantity = shoppingItemQuantity;
	}

	public ShoppingItem(ShoppingItemId itemId, Integer shoppingItemQuantity2, Integer productPrice,
			ShoppingOrder savedOrder) {
	}
    
	public ShoppingItem(ShoppingItemId id, Integer shoppingItemQuantity, Integer shoppingItemPrice) {
	    this.id = id;
	    this.shoppingItemQuantity = shoppingItemQuantity;
	    this.shoppingItemPrice = shoppingItemPrice; // 設置價格
	}

    
    

}
