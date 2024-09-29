package com.eatspan.SpanTasty.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.store.ShoppingItem;
import com.eatspan.SpanTasty.entity.store.ShoppingItemId;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, ShoppingItemId> {

	//DeleteAllItem
	@Modifying
    @Query("DELETE FROM ShoppingItem si WHERE si.shoppingOrder.id = :shoppingId")
    void deleteAllByShoppingOrderId(@Param("shoppingId") int shoppingId);
	
	
}
