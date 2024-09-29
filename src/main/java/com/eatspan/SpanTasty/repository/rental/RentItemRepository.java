package com.eatspan.SpanTasty.repository.rental;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.rental.RentItem.RentItemId;

@Repository
public interface RentItemRepository extends JpaRepository<RentItem, RentItemId> {
	
	@Query(value = "from RentItem where rentId = :rentId")
	List<RentItem> findRentItemsByRentId(Integer rentId);
	
	@Query(value = "from RentItem where rentId = :rentId and tablewareId = :tablewareId")
	RentItem findRentItemByRentItemId(Integer rentId, Integer tablewareId);
	
	
}
