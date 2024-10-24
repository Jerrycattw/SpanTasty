package com.eatspan.SpanTasty.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.store.ShoppingOrder;

public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, Integer> {

	List<ShoppingOrder> findByMemberId(Integer memberId);


}
