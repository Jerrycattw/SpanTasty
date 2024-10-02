package com.eatspan.SpanTasty.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.discount.PointSet;

public interface PointSetRepository extends JpaRepository<PointSet, String> {
	

	PointSet findByPointSetName(String pointSetName);
}
