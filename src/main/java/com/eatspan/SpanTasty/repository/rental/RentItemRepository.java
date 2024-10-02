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
	List<RentItem> findByRentId(Integer rentId);
	
	
	@Query(value = "from RentItem where rentId = :rentId and tablewareId = :tablewareId")
	RentItem findById(Integer rentId, Integer tablewareId);
	
	
	@Query(value = "DELETE FROM rent_item WHERE rent_id = :rentId", nativeQuery = true)
	int deleteByRentId(Integer rentId);
}
