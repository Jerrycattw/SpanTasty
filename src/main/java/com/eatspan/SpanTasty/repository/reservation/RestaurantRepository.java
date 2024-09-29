package com.eatspan.SpanTasty.repository.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.reservation.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}
