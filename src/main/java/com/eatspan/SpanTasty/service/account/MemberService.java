package com.eatspan.SpanTasty.service.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.config.MailConfig;
import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.repository.account.MemberRepository;
import com.eatspan.SpanTasty.utils.account.JwtUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConfig mailConfig;

	// 新增(註冊)Member
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
	
    public Member findMemberByShoppingId(Integer shoppingId) {
        return memberRepository.findMemberByShoppingId(shoppingId);
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

	// 更新會員資訊
	public boolean updateMemberInfo(Member member) {
	    try {
	        memberRepository.save(member); // 保存會員更新的資料到資料庫
	        return true;
	    } catch (Exception e) {
	        // 捕捉可能的例外，例如帳號或 email 重複等
	        return false;
	    }
	}
	
	// 更新會員頭像
	public boolean updateMemberAvatar(Integer memberId, byte[] avatarBytes) {

		Optional<Member> optionalMember = memberRepository.findById(memberId);
		if (!optionalMember.isPresent()) {
			return false; // 會員不存在，更新失敗
		}

		Member member = optionalMember.get();
		member.setAvatar(avatarBytes);

		memberRepository.save(member);

		return true;

	}

	// 獲取會員頭像數據
	public byte[] getMemberAvatar(Integer memberId) {
		// 查詢會員資料
		Optional<Member> optionalMember = memberRepository.findById(memberId);

		// 如果會員存在且有頭像，回傳頭像的 byte[] 數據
		if (optionalMember.isPresent()) {
			Member member = optionalMember.get();
			return member.getAvatar();
		}

		return null;
	}

	// 獲取所有會員(Rental使用 謝謝陳儒!)
	public List<Member> findAllMembers() {
		return memberRepository.findAll();
	}

	// 分頁查詢所有會員
	public Page<Member> findAllMembers(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

	// 分頁查詢所有會員，根據帳號
	public Page<Member> findByMemberNameContainingOrAccountContaining(String memberName, String account,
			Pageable pageable) {
		return memberRepository.findByMemberNameContainingOrAccountContaining(memberName, account, pageable);
	}

	// 更新會員狀態的邏輯
	public boolean updateMemberStatus(Integer memberId, char status, String reason, String suspendedUntil) {
		// 找到對應的會員
		Member member = memberRepository.findById(memberId).orElse(null);
		if (member == null) {
			return false; // 找不到會員，返回 false
		}

		// 設置新的狀態和原因
		member.setStatus(status);
		member.setReason(reason);

		// 如果狀態是暫停（status == 'S'），且有停權日期，則設置停權日期
		if (status == 'S' && suspendedUntil != null && !suspendedUntil.isEmpty()) {
			try {
				// 使用 LocalDate 解析，只需日期部分
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate suspendedDate = LocalDate.parse(suspendedUntil, formatter);
				member.setSuspendedUntil(suspendedDate.atStartOfDay()); // 將 LocalDate 轉換為 LocalDateTime 的午夜時間
			} catch (DateTimeParseException e) {
				e.printStackTrace();
				return false; // 日期解析失敗
			}
		} else {
			// 如果狀態不是暫停，清空停權日期
			member.setSuspendedUntil(null);
		}

		// 保存會員變更
		memberRepository.save(member);
		return true; // 更新成功
	}
	
	// 刪除頭像
	@Transactional
	public boolean removeMemberAvatar(Integer memberId) {
		try {
			Member member = memberRepository.findById(memberId).orElse(null);
			if (member == null) {
				return false;
			}
			member.setAvatar(null);
			memberRepository.save(member);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 忘記密碼：生成 JWT Token 並發送郵件
	public void createPasswordResetToken(String email, HttpServletRequest request) throws Exception {
		Member member = memberRepository.findByEmail(email);

		if (member == null) {
			throw new Exception("此信箱尚未註冊");
		}

		// 生成 JWT Token，並將 email 作為 claims 一部分
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", member.getEmail()); // 將 email 作為 token 的一部分
		String token = JwtUtil.genToken(claims);

		// 構建重設連結
		String resetLink = request.getRequestURL().toString().replace(request.getRequestURI(), "")
				+ "/SpanTasty/StarCups/resetPassword?token=" + token;

		// 發送郵件
		sendResetPasswordEmail(member.getEmail(), resetLink);
	}

	// 發送重設密碼的郵件
	private void sendResetPasswordEmail(String recipientEmail, String resetLink) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(mailConfig.getUserName()); // 設置發件人的電子郵件地址
		System.out.println(mailConfig.getUserName());
		
		helper.setTo(recipientEmail);
		System.out.println("寄收者 : " + recipientEmail);
		helper.setSubject("重設您的 SpanTasty 密碼");
		helper.setText("<p>您好，</p>" + "<p>請點擊下方連結重設您的密碼：</p>" + "<p><a href=\"" + resetLink + "\">重設密碼</a></p>", true);

		mailSender.send(message);
	}

	// 重設密碼邏輯
	public void resetPassword(String token, String newPassword) throws Exception {
		// 使用 JWT Token 解析出 email
		Map<String, Object> claims = JwtUtil.parseToken(token);
		String email = (String) claims.get("email");

		// 根據 email 查找用戶
		Member member = memberRepository.findByEmail(email);
		if (member == null) {
			throw new Exception("無效的重設密碼請求");
		}

		// 更新密碼
		member.setPassword(passwordEncoder.encode(newPassword));
		memberRepository.save(member);
	}

}
