package com.eatspan.SpanTasty.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.order.TogoItemEntity;
import com.eatspan.SpanTasty.entity.order.TogoItemId;

public interface TogoItemRepository extends JpaRepository<TogoItemEntity, TogoItemId> {

}
