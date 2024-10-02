package com.eatspan.SpanTasty.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.store.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {

	 @Query("SELECT pt FROM ProductType pt WHERE pt.productTypeName LIKE %:name%")
	    List<ProductType> findByProductTypeName(@Param("name") String name);
	
}
