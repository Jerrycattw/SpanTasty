package com.eatspan.SpanTasty.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.order.TogoItemEntity;
import com.eatspan.SpanTasty.entity.order.TogoItemId;

public interface TogoItemRepository extends JpaRepository<TogoItemEntity, TogoItemId> {
	
	@Query("SELECT t FROM TogoItemEntity t WHERE t.togoItemId.togoId = :togoId")
	List<TogoItemEntity> findByTogoId(@Param("togoId") Integer togoId);
}
