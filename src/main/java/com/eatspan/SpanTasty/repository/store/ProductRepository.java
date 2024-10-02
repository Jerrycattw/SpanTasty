package com.eatspan.SpanTasty.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eatspan.SpanTasty.entity.store.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("SELECT p FROM Product p JOIN FETCH p.productType")
    List<Product> findAllProduct();
}
