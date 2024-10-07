package com.eatspan.SpanTasty.controller.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.dto.account.AdminDto;
import com.eatspan.SpanTasty.entity.account.Admin;
import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.account.Permission;
import com.eatspan.SpanTasty.service.account.AdminService;
import com.eatspan.SpanTasty.service.account.MemberService;
import com.eatspan.SpanTasty.utils.account.Result;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private AdminService adminService;

	// 登入
	@PostMapping("/login")
	public Result<String> login(@RequestBody Map<String, String> loginData, HttpSession session) {
		String account = loginData.get("account");
		String password = loginData.get("password");

		Admin admin = adminService.login(account, password);

		// 如果登入成功
		if (admin != null) {
			// 檢查是否首次登入
			if (admin.getFirstLogin() == 1) {
				// 保存管理員的資訊到 Session 中
				session.setAttribute("admin", admin);
				return Result.success("首次登入，請修改個人資料", "/SpanTasty/admin/updateAdminInfoPage");
			}

			// 保存管理員的資訊到 Session 中
			session.setAttribute("admin", admin);
			session.setAttribute("permissions", admin.getPermissions());

			return Result.success("登入成功");
		} else {
			return Result.failure("帳號或密碼錯誤");
		}
	}

	// 檢查登入狀態
	@GetMapping("/checkLogin")
	public Result<Boolean> checkLogin(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		return admin != null ? Result.success(true) : Result.success(false);
	}

	// 登入信息
	@GetMapping("/info")
	public Result<Map<String, Object>> getAdminInfo(HttpSession session) {
	    Admin admin = (Admin) session.getAttribute("admin");

	    if (admin != null) {
	        Map<String, Object> data = new HashMap<>();
	        data.put("adminName", admin.getAdminName());
	        data.put("role", admin.getRole()); // 返回角色信息
	        
	        // 提取管理員的權限名稱列表
	        List<String> permissions = admin.getPermissions().stream()
	                                        .map(Permission::getPermissionName)
	                                        .collect(Collectors.toList());
	        data.put("permissions", permissions); // 返回權限信息

	        return Result.success(data);
	    } else {
	        return Result.failure("未登入");
	    }
	}

	// 登出
	@PostMapping("/logout")
	public Result<String> logout(HttpSession session) {
		session.invalidate(); // 使 session 無效
		return Result.success("登出成功");
	}

	// 新增管理員
	@PostMapping("/addAdmin")
	public Result<String> addAdmin(@RequestBody Map<String, Object> adminData) {
		List<String> permissions = (List<String>) adminData.get("permissions"); // 獲取權限列表
		System.out.println(permissions);
		// 創建新的 Admin 物件
		Admin admin = new Admin();
		int nextId = adminService.getNextAdminId();
		admin.setAdminName("Admin" + nextId);
		String account = "admin" + nextId;
		admin.setAccount(account);
		admin.setPassword(account); // 密碼與帳號相同
		admin.setStatus('A');
		admin.setRole(0);
		admin.setFirstLogin(1);

		// 調用 Service 層來保存管理員
		boolean isSuccess = adminService.addAdmin(admin);

		// 為新增的管理員設置權限
		if (isSuccess && permissions != null) {
			adminService.updateAdminPermissions(admin.getAdminId(), permissions);
		}

		if (isSuccess) {
			return Result.success("管理員新增成功，帳號為: " + account);
		} else {
			return Result.failure("管理員新增失敗");
		}
	}

	// 重設管理員
	@PostMapping("/resetAdmin")
	public Result<String> resetAdmin(@RequestParam Integer adminId) {
		System.out.println(adminId);
		boolean success = adminService.resetAdmin(adminId);

		if (success) {
			return Result.success("管理員重設成功！");
		} else {
			return Result.failure("重設管理員失敗，請稍後重試！");
		}
	}

	// 重設密碼
	// 重設密碼
	@PostMapping("/resetPassword")
	public Result<String> resetPassword(@RequestParam Integer adminId) {
		boolean success = adminService.resetPassword(adminId);

		if (success) {
			return Result.success("密碼重設成功！");
		} else {
			return Result.failure("重設密碼失敗，請稍後重試！");
		}
	}

	// 更新管理員資訊
	// 更新管理員資訊
	@PostMapping("/updateAdminProfile")
	public Result<String> updateProfile(@RequestBody Map<String, String> updateData, HttpSession session) {
		try {
			// 從 Session 中獲取當前管理員資訊
			Admin admin = (Admin) session.getAttribute("admin");
			if (admin == null) {
				return Result.failure("未登入或會話已過期，請重新登入");
			}

			// 更新管理員名稱與密碼
			String newAdminName = updateData.get("newAdminName");
			String newPassword = updateData.get("newPassword");
			String confirmPassword = updateData.get("confirmPassword");
			System.out.println(updateData);
			// 驗證新密碼和確認密碼是否一致
			if (newPassword != null && !newPassword.trim().isEmpty()) {
				if (!newPassword.equals(confirmPassword)) {
					return Result.failure("新密碼與確認密碼不一致，請重新輸入");
				}
			}

			// 調用服務層方法更新管理員資料
			boolean updateSuccess = adminService.updateAdminProfile(admin, newAdminName, newPassword);

			if (!updateSuccess) {
				return Result.failure("管理員資料更新失敗，請稍後再試");
			}

			// 更新 Session 中的管理員資訊
			session.setAttribute("admin", admin);

			return Result.success("管理員資料更新成功");
		} catch (Exception e) {
			return Result.failure("管理員資料更新失敗，請稍後再試");
		}
	}

	// 更新頭像
	@PostMapping("/updateAvatar")
	public Result<String> updateAdminAvatar(@RequestParam("avatar") MultipartFile avatarFile, HttpSession session) {
		try {
			// 從 session 中獲取管理員資料
			Admin admin = (Admin) session.getAttribute("admin");
			if (admin == null) {
				return Result.failure("未登入");
			}

			// 取得圖片的 byte[] 數據
			byte[] avatarBytes = avatarFile.getBytes();

			// 將頭像更新到數據庫或其他存儲
			boolean isSuccess = adminService.updateAdminAvatar(admin.getAdminId(), avatarBytes);

			// 如果更新成功，將頭像保存到 session
			if (isSuccess) {
				admin.setAvatar(avatarBytes); // 同步更新 session 中的管理員資料
				session.setAttribute("admin", admin); // 更新 session
				return Result.success("頭像更新成功");
			} else {
				return Result.failure("頭像更新失敗");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return Result.failure("頭像更新失敗，IO錯誤");
		}
	}

	// 移除管理員頭像
	@PostMapping("/removeAvatar")
	public Result<String> removeAvatar(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");

		if (admin == null) {
			return Result.failure("未登入或會話已過期");
		}

		boolean isRemoved = adminService.removeAdminAvatar(admin.getAdminId());

		if (isRemoved) {
			// 清空 session 中的頭像資料
			admin.setAvatar(null);
			session.setAttribute("admin", admin);
			return Result.success("頭像已成功移除");
		} else {
			return Result.failure("移除頭像失敗，請稍後再試");
		}
	}

	// 獲取頭像
	@GetMapping("/getAvatar")
	public Result<String> getAdminAvatar(HttpSession session) {
		// 從 session 中獲取管理員資料
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) {
			return Result.failure("未登入");
		}

		// 從 session 獲取頭像數據
		byte[] avatarBytes = admin.getAvatar();

		// 如果頭像為 null，返回默認圖片的訊息
		if (avatarBytes == null) {
			return Result.success("管理員未設置頭像", null);
		}

		// 將頭像轉換為 Base64 編碼並返回
		String base64Avatar = Base64.getEncoder().encodeToString(avatarBytes);
		return Result.success("頭像取得成功", base64Avatar);
	}

	// 搜索全部會員
	@GetMapping("/findMembers")
	public Result<Map<String, Object>> getAllMembers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String search) {

		Pageable pageable = PageRequest.of(page, size);

		// 如果有搜索條件，進行模糊查詢，否則查詢所有會員
		Page<Member> memberPage;
		if (search != null && !search.isEmpty()) {
			// 根據會員名稱或帳號進行模糊查詢
			memberPage = memberService.findByMemberNameContainingOrAccountContaining(search, search, pageable);
		} else {
			memberPage = memberService.findAllMembers(pageable);
		}

		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> membersList = new ArrayList<>();

		for (Member member : memberPage) {
			Map<String, Object> memberData = new HashMap<>();
			memberData.put("memberId", member.getMemberId());
			memberData.put("memberName", member.getMemberName());
			memberData.put("account", member.getAccount());
			memberData.put("status", member.getStatus());

			// 將 byte[] 照片數據轉換為 Base64 字串
			String avatarBase64 = member.getAvatar() != null
					? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(member.getAvatar())
					: null; // 如果沒有圖片，返回 null
			memberData.put("avatar", avatarBase64);

			membersList.add(memberData);
		}

		data.put("members", membersList);
		data.put("currentPage", memberPage.getNumber());
		data.put("totalItems", memberPage.getTotalElements());
		data.put("totalPages", memberPage.getTotalPages());

		return Result.success(data);
	}

	// 分頁查詢所有管理員
	@GetMapping("/findAllAdmins")
	public Result<Map<String, Object>> getAllAdmins(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		// 創建 Pageable 對象來控制分頁和排序
		Pageable pageable = PageRequest.of(page, size);
		Page<Admin> adminPage = adminService.findAllAdmins(pageable);

		// 將結果封裝到 Map 中
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> adminsList = new ArrayList<>();

		for (Admin admin : adminPage) {
			Map<String, Object> adminData = new HashMap<>();
			adminData.put("adminId", admin.getAdminId());
			adminData.put("adminName", admin.getAdminName());
			adminData.put("account", admin.getAccount());
			adminData.put("status", admin.getStatus());

			// 將 byte[] 照片數據轉換為 Base64 字串
			String avatarBase64 = admin.getAvatar() != null
					? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(admin.getAvatar())
					: null; // 如果沒有圖片，返回 null
			adminData.put("avatar", avatarBase64);

			adminsList.add(adminData);
		}

		data.put("admins", adminsList);
		data.put("currentPage", adminPage.getNumber());
		data.put("totalItems", adminPage.getTotalElements());
		data.put("totalPages", adminPage.getTotalPages());

		return Result.success(data);
	}

	// 根據名稱模糊查詢管理員
	@GetMapping("/searchAdmins")
	public Result<Map<String, Object>> searchAdmins(@RequestParam String adminName,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		// 創建 Pageable 對象來控制分頁和排序
		Pageable pageable = PageRequest.of(page, size);
		Page<Admin> adminPage = adminService.searchAdminsByName(adminName, pageable);

		// 將結果封裝到 Map 中
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> adminsList = new ArrayList<>();

		for (Admin admin : adminPage) {
			Map<String, Object> adminData = new HashMap<>();
			adminData.put("adminId", admin.getAdminId());
			adminData.put("adminName", admin.getAdminName());
			adminData.put("account", admin.getAccount());
			adminData.put("status", admin.getStatus());

			// 將 byte[] 照片數據轉換為 Base64 字串
			String avatarBase64 = admin.getAvatar() != null
					? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(admin.getAvatar())
					: null; // 如果沒有圖片，返回 null
			adminData.put("avatar", avatarBase64);

			adminsList.add(adminData);
		}

		data.put("admins", adminsList);
		data.put("currentPage", adminPage.getNumber());
		data.put("totalItems", adminPage.getTotalElements());
		data.put("totalPages", adminPage.getTotalPages());

		return Result.success(data);
	}

	// 更新會員狀態
	@PostMapping("/updateMemberStatus")
	public Result<String> updateMemberStatus(@RequestBody Map<String, Object> requestData) {
		Integer memberId = (Integer) requestData.get("memberId");
		// 將 status 轉換為 String，再轉換為 char
		String statusStr = (String) requestData.get("status");

		// 確認 statusStr 不為 null，並提取第一個字符
		char status = statusStr != null && !statusStr.isEmpty() ? statusStr.charAt(0) : 'A'; // 默認 'A'
		String reason = (String) requestData.get("reason");
		String suspendedUntil = (String) requestData.get("suspendedUntil"); // 可能為 null
		System.out.println("停權日期" + suspendedUntil);
		System.out.println("狀態" + status);

		// 調用 Service 層來更新會員狀態
		boolean isUpdated = memberService.updateMemberStatus(memberId, status, reason, suspendedUntil);

		if (isUpdated) {
			return Result.success("會員狀態更新成功");
		} else {
			return Result.failure("會員狀態更新失敗");
		}
	}
	
	//搜尋管理員權限
	@PostMapping("/searchAdminsByPermissions")
	public Result<List<Admin>> searchAdminsByPermissions(@RequestBody Map<String, Object> request) {
	    List<String> permissions = (List<String>) request.get("permissions");
	    int page = (int) request.get("page");
	    int size = (int) request.get("size");

	    // 檢查是否選擇了「無任何權限」
	    if (permissions.contains("none")) {
	        Page<Admin> adminPage = adminService.findAdminsWithNoPermissions(page, size);
	        return Result.success(adminPage.getContent());
	    }

	    Page<Admin> adminPage = adminService.findAdminsByPermissions(permissions, page, size);
	    return Result.success(adminPage.getContent());
	}
	
    // 獲取指定管理員的權限
    @GetMapping("/{adminId}/permissions")
    public Result<Set<Permission>> getAdminPermissions(@PathVariable Integer adminId) {
    	System.out.println(adminId);
        Admin admin = adminService.findById(adminId);
        
        if (admin == null) {
            return Result.failure("管理員未找到");
        }
        return Result.success(admin.getPermissions());
    }
    
    // 更新指定管理員的權限
    @PostMapping("/{adminId}/permissions")
    public Result<String> updateAdminPermissions(@PathVariable Integer adminId, @RequestBody Map<String, List<String>> request) {
        List<String> permissions = request.get("permissions");
        System.out.println(permissions);

        try {
            boolean updated = adminService.updatePermissions(adminId, permissions);
            if (updated) {
                return Result.success("權限更新成功");
            } else {
                return Result.failure("權限更新失敗，管理員未找到");
            }
        } catch (Exception e) {
            return Result.failure("更新過程中發生錯誤：" + e.getMessage());
        }
    }

}
