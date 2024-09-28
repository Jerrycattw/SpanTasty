package com.eatspan.SpanTasty.service.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.repository.account.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	//新增(註冊)Member
	public boolean save(Member member) {
		// 密碼加密
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		
		// 自動設定註冊日期和初始狀態
		member.setRegisterDate(LocalDate.now()); // 自動設定當前時間
		member.setStatus('A'); // 默認狀態為 Active
		
		try {
			memberRepository.save(member); // 保存會員到資料庫
			return true;
		} catch (Exception e) {
			// 捕捉可能的例外，例如帳號或 email 重複等
			return false;
		}
	}

	// 檢查帳號是否已經存在
	public boolean existsByAccount(String account) {
		return memberRepository.existsByAccount(account);
	}

	// 檢查信箱是否已經存在
	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
	
	public Member findMemberByAccount(String account) {
		return memberRepository.findByAccount(account);
	}
	
	public Optional<Member> findMemberById(Integer memberId) {
		return memberRepository.findById(memberId);
	}
	
    // 登入方法
    public Member login(String account, String rawPassword) {
        Member member = memberRepository.findByAccount(account);

        if (member == null) {
            return null; // 帳號不存在
        }

        // 比對密碼
        if (passwordEncoder.matches(rawPassword, member.getPassword())) {
            // 更新登入時間
            member.setLoginDate(LocalDateTime.now());
            memberRepository.save(member); // 保存會員更新信息
            return member; // 登入成功
        } else {
            return null; // 密碼錯誤
        }
    }

    // 更新密碼方法
    public boolean updatePassword(Integer memberId, String oldPassword, String newPassword) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (!optionalMember.isPresent()) {
            return false; // 會員不存在
        }

        Member member = optionalMember.get();

        // 檢查舊密碼是否正確
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            return false; // 舊密碼不正確
        }

        // 更新密碼，並加密新密碼
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member); // 保存更新的會員信息
        return true; // 密碼更新成功
    }

}
    

