package com.eatspan.SpanTasty.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.store.ShoppingOrder;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Integer> {

}