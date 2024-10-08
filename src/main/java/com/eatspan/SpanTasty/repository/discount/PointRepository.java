package com.eatspan.SpanTasty.repository.discount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eatspan.SpanTasty.dto.discount.PointCenterDTO;
import com.eatspan.SpanTasty.dto.discount.PointMemberProjection;
import com.eatspan.SpanTasty.entity.discount.Point;

public interface PointRepository extends JpaRepository<Point, Integer> {
	
	List<Point> findByMemberId(Integer memberId);
	
	@Query("SELECT p  FROM Point p WHERE (p.pointUsage IS NOT NULL or p.pointUsage <> 0) AND p.expiryDate > CURRENT_DATE AND p.memberId= :memberId ORDER BY p.expiryDate ")
	List<Point> findByMemberIdBeforeUsePoint(Integer memberId);
	
	List<Point> findByPointId(Integer pointId);
	
	
	@Query(nativeQuery = true, value = """
	        WITH point_summary AS (
	            SELECT member_id, SUM(COALESCE(point_usage, 0)) AS total_point
	            FROM point
	            GROUP BY member_id
	        ),
	        expiry_points AS (
	            SELECT member_id, SUM(COALESCE(point_usage, 0)) AS total_point, get_expiry_date,
	                   ROW_NUMBER() OVER (PARTITION BY member_id ORDER BY get_expiry_date) AS rank
	            FROM point
				where get_expiry_date IS NOT NULL　and point_usage>0
				AND get_expiry_date > GETDATE()
	            GROUP BY get_expiry_date, member_id
	        )
	        SELECT 
					ps.member_id AS memberId, 
	            　m.member_name AS memberName, 
	               m.phone AS phone, 
	               ps.total_point AS totalPointBalance,
	               ep.total_point AS expiringPoints, 
	               ep.get_expiry_date AS expiryDate
	        FROM point_summary ps 
	        LEFT JOIN expiry_points ep ON ps.member_id = ep.member_id
	        LEFT JOIN members m ON ps.member_id = m.member_id
	        WHERE ep.rank = 1 or ep.rank is Null
	    """)
	List<PointMemberProjection> findPointMembers();
	
	
	@Query(value ="""
	       WITH point_summary AS (
	            SELECT member_id, SUM(COALESCE(point_usage, 0)) AS total_point
	            FROM point
	            GROUP BY member_id
	        ),
	        expiry_points AS (
	            SELECT member_id, SUM(COALESCE(point_usage, 0)) AS total_point, get_expiry_date,
	                   ROW_NUMBER() OVER (PARTITION BY member_id ORDER BY get_expiry_date) AS rank
	            FROM point
				where get_expiry_date IS NOT NULL　and point_usage>0
				AND get_expiry_date > GETDATE()
	            GROUP BY get_expiry_date, member_id
	        )
	        SELECT 
					ps.member_id AS memberId, 
	            　m.member_name AS memberName, 
	               m.phone AS phone, 
	               ps.total_point AS totalPointBalance,
	               ep.total_point AS expiringPoints, 
	               ep.get_expiry_date AS expiryDate
	        FROM point_summary ps 
	        LEFT JOIN expiry_points ep ON ps.member_id = ep.member_id
	        LEFT JOIN members m ON ps.member_id = m.member_id
	        WHERE (ep.rank = 1 or ep.rank is Null) AND ps.member_id=:memberId
	        """,nativeQuery = true)
	PointMemberProjection findPointMembersByMemberId(@Param("memberId") Integer memberId);
	
	
	@Query(value ="""
	        WITH point_summary AS (
	            SELECT member_id, SUM(COALESCE(point_usage, 0)) AS total_point
	            FROM point
	            GROUP BY member_id
	        ),
	        expiry_points AS (
	            SELECT member_id, SUM(COALESCE(point_usage, 0)) AS total_point, get_expiry_date,
	                   ROW_NUMBER() OVER (PARTITION BY member_id ORDER BY get_expiry_date) AS rank
	            FROM point
				where get_expiry_date IS NOT NULL　and point_usage>0
				AND get_expiry_date > GETDATE()
	            GROUP BY get_expiry_date, member_id
	        )
	        SELECT 
					ps.member_id AS memberId, 
	            　m.member_name AS memberName, 
	               m.phone AS phone, 
	               ps.total_point AS totalPointBalance,
	               ep.total_point AS expiringPoints, 
	               ep.get_expiry_date AS expiryDate
	        FROM point_summary ps 
	        LEFT JOIN expiry_points ep ON ps.member_id = ep.member_id
	        LEFT JOIN members m ON ps.member_id = m.member_id
	        WHERE (ep.rank = 1 or ep.rank is Null) AND (m.phone LIKE %:keyWord% OR m.member_id LIKE %:keyWord% OR m.member_name LIKE %:keyWord%)
	        """,nativeQuery = true)
	List<PointMemberProjection> searchPointMembers(@Param(value = "keyWord") String keyWord);
	
	@Query("SELECT NEW com.eatspan.SpanTasty.dto.discount.PointCenterDTO(" +
			   " p.transactionType, "+
	           "SUM(CASE WHEN p.pointChange > 0 THEN p.pointChange ELSE 0 END), " +//累計發放
	           "SUM(CASE WHEN p.pointChange > 0 THEN p.pointChange - p.pointUsage ELSE 0 END), " +//累計兌換
	           "SUM(CASE WHEN p.expiryDate < CURRENT_DATE THEN p.pointUsage ELSE 0 END)) " +//累計過期
	           "FROM Point p GROUP BY p.transactionType")
  	List<PointCenterDTO> sumPointsByTrans();

	@Query("SELECT NEW com.eatspan.SpanTasty.dto.discount.PointCenterDTO(" +
	           "SUM(CASE WHEN p.pointChange > 0 THEN p.pointChange ELSE 0 END), " +//累計發放
	           "SUM(CASE WHEN p.pointChange > 0 THEN p.pointChange - p.pointUsage ELSE 0 END), " +//累計兌換
	           "SUM(CASE WHEN p.expiryDate < CURRENT_DATE THEN p.pointUsage ELSE 0 END)) " +//累計過期
	           "FROM Point p")
	PointCenterDTO sumPointsAll();
	
}
