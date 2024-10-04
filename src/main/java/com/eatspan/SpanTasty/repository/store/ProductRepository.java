package com.eatspan.SpanTasty.repository.store;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.store.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query("SELECT p FROM Product p JOIN FETCH p.productType")
    List<Product> findAllProduct();
	
	
	@Query("SELECT p.productPrice FROM Product p WHERE p.id = :productId")
	Integer findProductPriceByProductId(@Param("productId") Integer productId);
}