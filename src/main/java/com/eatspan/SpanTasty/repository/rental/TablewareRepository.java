package com.eatspan.SpanTasty.repository.rental;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.rental.Tableware;

@Repository
public interface TablewareRepository extends JpaRepository<Tableware, Integer> {
	
	
	@Query(value = "SELECT tableware_id FROM tableware", nativeQuery = true)
	List<Integer> findTablwareIds();
	
	
	@Query(value = "FROM Tableware WHERE tablewareName LIKE %:keyword% OR tablewareDescription LIKE %:keyword%")
	List<Tableware> findByKeyword(@Param("keyword") String keyword);
	
	
	@Query(value = "SELECT COUNT(*) FROM Tableware", nativeQuery = true)
	Integer countTableware();
}
