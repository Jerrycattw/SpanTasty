package com.eatspan.SpanTasty.controller.account;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.utils.account.JwtUtil;
import com.eatspan.SpanTasty.utils.account.Result;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private MemberService memberService;

	// 註冊會員
	@PostMapping("/register")
	public Result<Member> registerMember(@RequestBody Member member, @RequestParam String confirmPassword) {
		// 檢查帳號是否已存在
		if (memberService.existsByAccount(member.getAccount())) {
			return Result.failure("帳號已存在");
		}

		if (memberService.existsByEmail(member.getEmail())) {
			return Result.failure("此信箱已被使用");
		}

		// 檢查兩次密碼是否一致
		if (!member.getPassword().equals(confirmPassword)) {
			return Result.failure("兩次輸入的密碼不一致");
		}

		// 嘗試保存會員資料
		boolean isSuccess = memberService.save(member);
		if (isSuccess) {
			return Result.success();
		} else {
			return Result.failure("註冊失敗，請重試");
		}
	}

	// 登入
	@PostMapping("/login")
	public Result<String> login(@RequestParam String account, @RequestParam String password) {
		Member member = memberService.login(account, password);

		if (member == null) {
			return Result.failure("登入失敗，帳號或密碼錯誤");
		}

		Map<String, Object> claims = new HashMap<>();
		claims.put("memberId", member.getMemberId());
		claims.put("memberName", member.getMemberName());

		// 生成 JWT token
		String token = JwtUtil.genToken(claims);

		// 返回包含 token 的成功結果
		return Result.success("登入成功", token);
	}

	// 查詢個人資訊
	@GetMapping("/profile")
	public Result<Optional<Member>> getMemberProfile(@RequestHeader("Authorization") String token) {

		// 解析 JWT token 取得 claims
		Map<String, Object> claims = JwtUtil.parseToken(token);

		// 取得會員 ID
		Integer memberId = (Integer) claims.get("memberId");

		if (memberId == null) {
			return Result.failure("無法從 Token 中取得會員 ID");
		}

		// 根據會員 ID 查詢個人資訊
		Optional<Member> member = memberService.findMemberById(memberId);

		if (member != null) {
			return Result.success("個人資訊查詢成功", member);
		} else {
			return Result.failure("無法找到該會員的個人資訊");
		}
	}
	
	//更新密碼
	@PostMapping("/updatePassword")
	public Result<String> updateMemberPassword(@RequestHeader("Authorization") String token, @RequestParam String oldPassword,
			@RequestParam String newPassword, @RequestParam String confirmPassword) {
		// 解析 JWT token 來獲取 memberId
		Map<String, Object> claims = JwtUtil.parseToken(token);
		Integer memberId = (Integer) claims.get("memberId");

		// 檢查新密碼和確認密碼是否一致
		if (!newPassword.equals(confirmPassword)) {
			return Result.failure("兩次輸入的密碼不一致");
		}

		// 調用 Service 方法來更新密碼
		boolean isSuccess = memberService.updatePassword(memberId, oldPassword, newPassword);

		if (isSuccess) {
			return Result.success("密碼重設成功");
		} else {
			return Result.failure("舊密碼不正確");
		}
	}

	//更新頭像
	@PostMapping("/updateAvatar")
	public Result<String> updateMemberAvatar(@RequestParam("avatar") MultipartFile avatarFile,
			@RequestHeader("Authorization") String token) {
        try {
            // 解析 JWT Token 並取得會員 ID
            Map<String, Object> claims = JwtUtil.parseToken(token.replace("Bearer ", ""));
            Integer memberId = (Integer) claims.get("memberId");

            // 取得圖片的 byte[] 數據
            byte[] avatarBytes = avatarFile.getBytes();

            // 更新會員頭像並取得操作結果
            boolean isSuccess = memberService.updateMemberAvatar(memberId, avatarBytes);

            // 根據操作結果回傳相應的 Result
            if (isSuccess) {
                return Result.success("更新圖片成功");
            } else {
                return Result.failure("更新圖片失敗");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure("頭像更新失敗，IO錯誤");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("頭像更新失敗");
        }
	}

	
	//獲得頭像
	@GetMapping("/getAvatar")
	public Result<String> getAvatar(@RequestHeader("Authorization") String token) {
		Map<String, Object> claims = JwtUtil.parseToken(token);
		Integer memberId = (Integer) claims.get("memberId");
		
		byte[] avatarBytes = memberService.getMemberAvatar(memberId);
		
        if (avatarBytes == null) {
            // 如果頭像為 null，回傳告知前端使用預設圖片
            return Result.success("用戶未設定頭像", null);
        }
        
        String base64Avatar = Base64.getEncoder().encodeToString(avatarBytes);
        
        return Result.success("頭像取得成功", base64Avatar);
		
	}

}
