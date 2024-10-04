package com.eatspan.SpanTasty.repository.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.reservation.Reserve;

public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	
	//查詢訂位資料by可變條件
	@Query("SELECT r FROM Reserve r JOIN r.restaurant rt JOIN r.member m WHERE 1=1 "
	        + "AND (:memberName IS NULL OR m.memberName LIKE %:memberName%) "
	        + "AND (:phone IS NULL OR m.phone LIKE %:phone%) "
	        + "AND (:restaurantId IS NULL OR rt.restaurantId = :restaurantId) "
	        + "AND (:tableTypeId IS NULL OR r.tableType.tableTypeId = :tableTypeId) "
	        + "AND (:reserveTimeStart IS NULL OR r.reserveTime >= :reserveTimeStart) "
	        + "AND (:reserveTimeEnd IS NULL OR r.reserveTime <= :reserveTimeEnd)"
	        + "ORDER BY r.reserveTime DESC")
	List<Reserve> findReserveByCriteria(
	        @Param("memberName") String memberName,
	        @Param("phone") String phone,
	        @Param("restaurantId") Integer restaurantId,
	        @Param("tableTypeId") String tableTypeId,
	        @Param("reserveTimeStart") LocalDateTime reserveTimeStart,
	        @Param("reserveTimeEnd") LocalDateTime reserveTimeEnd
	);
	
	

    // 查詢某個時間段內的預訂數量
    @Query("SELECT COUNT(r) FROM Reserve r WHERE r.restaurant.id = :restaurantId "
            + "AND r.tableType.id = :tableTypeId "
            + "AND CAST(r.reserveTime AS DATE) = CAST(:checkDate AS DATE) "
            + "AND CAST(r.reserveTime AS TIME) < CAST(:slotEnd AS TIME)"
            + "AND CAST(r.finishedTime AS TIME) > CAST(:slotStart AS TIME)")
    Integer countReservationsInTimeSlot(@Param("restaurantId") Integer restaurantId,
                                        @Param("tableTypeId") String tableTypeId,
                                        @Param("checkDate") LocalDate checkDate,
                                        @Param("slotStart") LocalTime slotStart,
                                        @Param("slotEnd") LocalTime slotEnd);

    // 查詢該餐廳和桌型下的總桌數
    @Query("SELECT COUNT(rt) FROM RestaurantTable rt WHERE rt.restaurant.id = :restaurantId "
            + "AND rt.tableType.id = :tableTypeId")
    Integer countAvailableTables(@Param("restaurantId") Integer restaurantId,
                                 @Param("tableTypeId") String tableTypeId);

	
	
	
	
	

}
