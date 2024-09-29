package com.eatspan.SpanTasty.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.reservation.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {

}
