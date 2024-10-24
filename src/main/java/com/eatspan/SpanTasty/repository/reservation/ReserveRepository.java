package com.eatspan.SpanTasty.repository.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.eatspan.SpanTasty.entity.account.Member;


public interface ReserveRepository extends JpaRepository<Reserve, Integer> {
	
	//查詢訂位資料by可變條件(後台查詢)
	@Query("SELECT r FROM Reserve r JOIN r.restaurant rt JOIN r.member m WHERE 1=1 "
	        + "AND (:memberName IS NULL OR m.memberName LIKE %:memberName%) "
	        + "AND (:phone IS NULL OR m.phone LIKE %:phone%) "
	        + "AND (:restaurantId IS NULL OR rt.restaurantId = :restaurantId) "
	        + "AND (:tableTypeId IS NULL OR r.tableType.tableTypeId = :tableTypeId) "
	        + "AND (:reserveTimeStart IS NULL OR CAST(r.reserveTime AS DATE) >= :reserveTimeStart) "
	        + "AND (:reserveTimeEnd IS NULL OR CAST(r.reserveTime AS DATE) <= :reserveTimeEnd)"
	        + "ORDER BY r.reserveTime DESC")
	List<Reserve> findReserveByCriteria(
	        @Param("memberName") String memberName,
	        @Param("phone") String phone,
	        @Param("restaurantId") Integer restaurantId,
	        @Param("tableTypeId") String tableTypeId,
	        @Param("reserveTimeStart") LocalDate reserveTimeStart,
	        @Param("reserveTimeEnd") LocalDate reserveTimeEnd
	);
	
	
	//查詢訂位資料by可變條件(某日所有訂位)
//	@Query("SELECT r FROM Reserve r JOIN r.restaurant rt JOIN r.member m WHERE 1=1 "
//			+ "AND r.restaurant.id = :restaurantId "
//			+ "AND CAST(r.reserveTime AS DATE) = CAST(:checkDate AS DATE) "
//			+ "ORDER BY r.reserveTime DESC")
//	List<Reserve> findReserveByRestaurantAndDate(@Param("restaurantId") Integer restaurantId,
//												 @Param("checkDate") LocalDate checkDate);
	
	

    // 查詢單一餐廳某種桌位某個時間段內的預訂數量(訂位)
    @Query("SELECT COUNT(r) FROM Reserve r WHERE r.restaurant.id = :restaurantId "
            + "AND r.tableType.id = :tableTypeId "
            + "AND CAST(r.reserveTime AS DATE) = CAST(:checkDate AS DATE) "
            + "AND CAST(r.reserveTime AS TIME) < CAST(:slotEnd AS TIME) "
            + "AND CAST(r.finishedTime AS TIME) > CAST(:slotStart AS TIME)")
    Integer countReservationsInTimeSlot(@Param("restaurantId") Integer restaurantId,
                                        @Param("tableTypeId") String tableTypeId,
                                        @Param("checkDate") LocalDate checkDate,
                                        @Param("slotStart") LocalTime slotStart,
                                        @Param("slotEnd") LocalTime slotEnd);
    

    // 查詢該餐廳和桌型下的總桌數(訂位)
    @Query("SELECT COUNT(rt) FROM RestaurantTable rt WHERE rt.restaurant.id = :restaurantId "
            + "AND rt.tableType.id = :tableTypeId "
            + "AND rt.tableStatus = 1 ")
    Integer countAvailableTables(@Param("restaurantId") Integer restaurantId,
                                 @Param("tableTypeId") String tableTypeId);
    
    
    // 查詢單一餐廳某個日期時間段內的預訂數量(統計)
    @Query("SELECT COUNT(r) FROM Reserve r WHERE r.restaurant.id = :restaurantId "
            + "AND CAST(r.reserveTime AS DATE) BETWEEN :startDate AND :endDate")
    Integer countReservationsInDateSlot(@Param("restaurantId") Integer restaurantId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);
    
    
    // 查詢所有分店某個日期時間段內的預訂數量(統計)
    @Query("SELECT COUNT(r) FROM Reserve r WHERE CAST(r.reserveTime AS DATE) BETWEEN :startDate AND :endDate")
    Integer countReservationsInMonth(@Param("startDate") LocalDate startDate,
    								 @Param("endDate") LocalDate endDate);
    
    
 // 使用原生 SQL 查詢單一餐廳某星期幾的預訂數量
    @Query(value = "SELECT COUNT(*) FROM Reserve r WHERE r.restaurant_id = :restaurantId "
    		+ "AND DATEPART(WEEKDAY, r.reserve_time) = :weekDay", nativeQuery = true)
    Integer countReservationsByWeekDay(@Param("restaurantId") Integer restaurantId,
    								   @Param("weekDay") Integer weekDay);
    
    
    // 查詢單一餐廳某個訂位人數的預訂數量(統計)
    @Query("SELECT COUNT(r) FROM Reserve r WHERE r.restaurant.id = :restaurantId "
    		+ "AND r.reserveSeat = :seat")
    Integer countReservationsBySeat(@Param("restaurantId") Integer restaurantId,
    								@Param("seat") Integer seat);
    
    
    


	
	// 查詢會員所有訂位紀錄
	List<Reserve> findByMemberOrderByReserveTimeDesc(Member member);
	
	

}
