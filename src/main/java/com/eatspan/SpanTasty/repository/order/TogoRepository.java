package com.eatspan.SpanTasty.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.order.TogoEntity;

public interface TogoRepository extends JpaRepository<TogoEntity, Integer> {
	
	List<TogoEntity> findByMemberId(Integer memberId);
	List<TogoEntity> findTogoByTgPhoneContaining(String tgPhone);
	List<TogoEntity> findByTgNameAndTgPhone(String tgName, String tgPhone);
	
}
