package com.eatspan.SpanTasty.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.reservation.RestaurantTable;
import com.eatspan.SpanTasty.entity.reservation.RestaurantTableId;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, RestaurantTableId> {

}
