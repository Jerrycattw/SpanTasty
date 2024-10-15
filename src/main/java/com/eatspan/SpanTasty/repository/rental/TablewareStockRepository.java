package com.eatspan.SpanTasty.repository.rental;


import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;

@Repository
public interface TablewareStockRepository extends JpaRepository<TablewareStock, TablewareStock.TablewareStockId> {
	
	
	@Query("from TablewareStock where"
            + "(:tablewareId IS NULL OR tablewareId = :tablewareId) AND "
            + "(:restaurantId IS NULL OR restaurantId = :restaurantId)")
	List<TablewareStock> findByCriteria(
	        @Param("tablewareId") Integer tablewareId,
	        @Param("restaurantId") Integer restaurantId);
	
	
	@Query("from TablewareStock where tablewareId = :tablewareId AND restaurantId = :restaurantId")
	TablewareStock findById(@Param("tablewareId") Integer tablewareId, @Param("restaurantId") Integer restaurantId);
	
	
	@Query(value = "from TablewareStock where tablewareId = :tablewareId")
	List<TablewareStock> findByTablewareId(Integer tablewareId);
	
	
	List<TablewareStock> findByRestaurantId(Integer restaurantId);
}
