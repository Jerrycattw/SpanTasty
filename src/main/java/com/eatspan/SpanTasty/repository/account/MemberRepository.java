package com.eatspan.SpanTasty.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eatspan.SpanTasty.entity.account.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	//帳號是否存在
	boolean existsByAccount(String account);
	
	//email是否存在
	boolean existsByEmail(String email);
	
	//根據帳號尋找會員
	Member findByAccount(String account);

}
