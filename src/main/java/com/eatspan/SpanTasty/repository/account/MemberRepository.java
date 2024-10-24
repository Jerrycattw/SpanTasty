package com.eatspan.SpanTasty.repository.account;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.account.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	// 帳號是否存在
	boolean existsByAccount(String account);

	// email是否存在
	boolean existsByEmail(String email);

	Member findByEmail(String email);

	// 根據帳號尋找會員
	Member findByAccount(String account);

	// 尋找會員
	Page<Member> findByMemberNameContainingOrAccountContaining(String memberName, String account, Pageable pageable);

	Member findByProviderId(String googleId);

	@Query("SELECT FUNCTION('FORMAT', registerDate, 'yyyy-MM') AS month, COUNT(m) FROM Member m GROUP BY FUNCTION('FORMAT', registerDate, 'yyyy-MM')")
	List<Object[]> findMonthlyRegistrationCounts();

	@Query("SELECT m.status, COUNT(m) FROM Member m GROUP BY m.status")
	List<Object[]> findMemberStatusCounts();

	// 計算特定年份和月份的註冊會員數，使用 'yyyy-MM' 格式
	@Query("SELECT COUNT(m) FROM Member m WHERE FORMAT(m.registerDate, 'yyyy-MM') = :yearMonth")
	int countMembersByYearMonth(@Param("yearMonth") String yearMonth);

	@Query("FROM Member m JOIN ShoppingOrder s ON m.id = s.member.id WHERE s.id = :shoppingId")
	Member findMemberByShoppingId(@Param("shoppingId") Integer shoppingId);

	// 查詢總會員數
	@Query("SELECT COUNT(m) FROM Member m")
	Integer countTotalMembers();

	// 查詢當月會員數
	@Query("SELECT COUNT(m) FROM Member m WHERE MONTH(m.registerDate) = MONTH(CURRENT_DATE) AND YEAR(m.registerDate) = YEAR(CURRENT_DATE)")
	Integer countMembersRegisteredThisMonth();

	// 查詢當月登入會員數
	@Query("SELECT COUNT(m) FROM Member m WHERE MONTH(m.loginDate) = MONTH(CURRENT_DATE) AND YEAR(m.loginDate) = YEAR(CURRENT_DATE)")
	Integer countActiveMembersThisMonth();

	// 查詢停權會員數
	@Query("SELECT COUNT(m) FROM Member m WHERE m.status IN ('S', 'I')")
	Integer countSuspendedAndInactiveMembers();
	
	// 查詢會員年齡分布
	@Query("SELECT " + "SUM(CASE WHEN DATEDIFF(YEAR, m.birthday, CURRENT_DATE) < 18 THEN 1 ELSE 0 END) AS ageBelow18, "
			+ "SUM(CASE WHEN DATEDIFF(YEAR, m.birthday, CURRENT_DATE) BETWEEN 18 AND 25 THEN 1 ELSE 0 END) AS age18To25, "
			+ "SUM(CASE WHEN DATEDIFF(YEAR, m.birthday, CURRENT_DATE) BETWEEN 26 AND 35 THEN 1 ELSE 0 END) AS age26To35, "
			+ "SUM(CASE WHEN DATEDIFF(YEAR, m.birthday, CURRENT_DATE) BETWEEN 36 AND 45 THEN 1 ELSE 0 END) AS age36To45, "
			+ "SUM(CASE WHEN DATEDIFF(YEAR, m.birthday, CURRENT_DATE) > 45 THEN 1 ELSE 0 END) AS age46Plus "
			+ "FROM Member m")
	Map<String, Integer> countMembersByAgeGroup();
	
	// 會員登入日期查詢人數
	@Query(value = "SELECT CONVERT(DATE, m.login_date) AS date, COUNT(m.member_id) AS activeMembers " +
            "FROM members m " +
            "WHERE YEAR(m.login_date) = :year AND MONTH(m.login_date) = :month " +
            "GROUP BY CONVERT(DATE, m.login_date) " +
            "ORDER BY CONVERT(DATE, m.login_date)", 
    nativeQuery = true)
     List<Object[]> findActiveMembersByLoginDateForMonth(@Param("year") int year, @Param("month") int month);
     
     // 每月的會員註冊數據
     @Query("SELECT MONTH(m.registerDate) as month, COUNT(m) as registrations " +
             "FROM Member m WHERE YEAR(m.registerDate) = :year GROUP BY MONTH(m.registerDate) ORDER BY month")
      List<Map<String, Object>> findMonthlyRegistrationsByYear(@Param("year") int year);

}
