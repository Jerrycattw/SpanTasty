package com.eatspan.SpanTasty.repository.discount;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.discount.CouponSchedule;

public interface CouponScheduleRepository extends JpaRepository<CouponSchedule, Integer> {
	
	@Query("FROM CouponSchedule cs WHERE cs.scheduleTime <= :time AND cs.status = :status")
	List<CouponSchedule> findCouponScheduleByStausAndBeforTime(@Param(value = "status")String status, @Param(value = "time")LocalDateTime time);
}
