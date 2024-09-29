package com.eatspan.SpanTasty.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.store.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
}
