package com.eatspan.SpanTasty.repository.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.account.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	//帳號是否存在
	boolean existsByAccount(String account);
	
	//email是否存在
	boolean existsByEmail(String email);
	
	//根據帳號尋找會員
	Member findByAccount(String account);
	
	// 尋找會員
    Page<Member> findByMemberNameContainingOrAccountContaining(String memberName, String account, Pageable pageable);

    
    @Query("SELECT FUNCTION('FORMAT', registerDate, 'yyyy-MM') AS month, COUNT(m) FROM Member m GROUP BY FUNCTION('FORMAT', registerDate, 'yyyy-MM')")
    List<Object[]> findMonthlyRegistrationCounts();

    @Query("SELECT m.status, COUNT(m) FROM Member m GROUP BY m.status")
    List<Object[]> findMemberStatusCounts();
}
