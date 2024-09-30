package com.eatspan.SpanTasty.repository.rental;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.eatspan.SpanTasty.entity.rental.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Integer> {
	
	@Query(value = "from Rent where"
            + "(:rentId IS NULL OR rentId = :rentId) AND "
            + "(:memberId IS NULL OR memberId = :memberId) AND "
            + "(:restaurantId IS NULL OR restaurantId = :restaurantId) AND "
            + "(:rentStatus IS NULL OR rentStatus = :rentStatus) AND "
            + "(:rentDateStart IS NULL OR :rentDateEnd IS NULL OR rentDate BETWEEN :rentDateStart AND :rentDateEnd)")
	List<Rent> findRentsByCriteria(
			@Param("rentId") Integer rentId,
	        @Param("memberId") Integer memberId,
	        @Param("restaurantId") Integer restaurantId,
	        @Param("rentStatus") Integer rentStatus,
	        @Param("rentDateStart") Date rentDateStart,
	        @Param("rentDateEnd") Date rentDateEnd);
	
	@Query(value = "SELECT * FROM rent WHERE due_date < current_date() and return_date is null", nativeQuery = true)
	List<Rent> findRentsByOverDueDate();
	
	@Query(value = "SELECT rent_id FROM rent", nativeQuery = true)
	List<Integer> findRentIds();
}