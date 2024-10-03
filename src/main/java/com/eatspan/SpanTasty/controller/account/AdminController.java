package com.eatspan.SpanTasty.controller.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eatspan.SpanTasty.entity.account.Admin;
import com.eatspan.SpanTasty.entity.account.Member;
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
            // 保存管理員的資訊到 Session 中
            session.setAttribute("admin", admin);
            session.setAttribute("permissions", admin.getPermissions());

            return Result.success("登入成功");
        } else {
            return Result.failure("帳號或密碼錯誤");
        }
    }
    
    // 登出
    @GetMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate(); // 使 session 無效
        return Result.success("登出成功");
    }
    
    //登入信息
    @GetMapping("/info")
    public Result<Map<String, String>> getAdminInfo(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        
        if (admin != null) {
            Map<String, String> data = new HashMap<>();
            data.put("adminName", admin.getAdminName());  // 假設 Admin 有 getAdminName() 方法
            return Result.success(data);
        } else {
            return Result.failure("未登入");
        }
    }
    
    //更新頭像
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
                session.setAttribute("adminAvatar", avatarBytes);
                return Result.success("頭像更新成功");
            } else {
                return Result.failure("頭像更新失敗");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure("頭像更新失敗，IO錯誤");
        }
    }
    
    //獲取頭像
    @GetMapping("/getAvatar")
    public Result<String> getAdminAvatar(HttpSession session) {
        // 從 session 中獲取管理員資料
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return Result.failure("未登入");
        }

        // 從 session 獲取頭像數據
        byte[] avatarBytes = (byte[]) session.getAttribute("adminAvatar");

        // 如果頭像為 null，返回默認圖片的訊息
        if (avatarBytes == null) {
            return Result.success("管理員未設置頭像", null);
        }

        // 將頭像轉換為 Base64 編碼並返回
        String base64Avatar = Base64.getEncoder().encodeToString(avatarBytes);
        return Result.success("頭像取得成功", base64Avatar);
    }


    
    //搜索全部會員 
    @GetMapping("/findMembers")
    public Result<Map<String, Object>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

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

        // 將每個會員的資料和照片轉換為 Base64 格式
        for (Member member : memberPage) {
            Map<String, Object> memberData = new HashMap<>();
            memberData.put("memberId", member.getMemberId());
            memberData.put("memberName", member.getMemberName());
            memberData.put("account", member.getAccount());
            memberData.put("status", member.getStatus());

            // 將 byte[] 照片數據轉換為 Base64 字串
            String avatarBase64 = member.getAvatar() != null 
                ? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(member.getAvatar())
                : null;  // 如果沒有圖片，返回 null
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
    public Result<Map<String, Object>> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 創建 Pageable 對象來控制分頁和排序
        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> adminPage = adminService.findAllAdmins(pageable);

        // 將結果封裝到 Map 中
        Map<String, Object> data = new HashMap<>();
        data.put("admins", adminPage.getContent());
        data.put("currentPage", adminPage.getNumber());
        data.put("totalItems", adminPage.getTotalElements());
        data.put("totalPages", adminPage.getTotalPages());

        return Result.success(data);
    }
    
    // 根據名稱模糊查詢管理員
    @GetMapping("/searchAdmins")
    public Result<Map<String, Object>> searchAdmins(
            @RequestParam String adminName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // 創建 Pageable 對象來控制分頁和排序
        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> adminPage = adminService.searchAdminsByName(adminName, pageable);

        // 將結果封裝到 Map 中
        Map<String, Object> data = new HashMap<>();
        data.put("admins", adminPage.getContent());
        data.put("currentPage", adminPage.getNumber());
        data.put("totalItems", adminPage.getTotalElements());
        data.put("totalPages", adminPage.getTotalPages());

        return Result.success(data);
    }
    
    //更新會員狀態
    @PostMapping("/updateMemberStatus")
    public Result<String> updateMemberStatus(@RequestBody Map<String, Object> requestData) {
        Integer memberId = (Integer) requestData.get("memberId");
        // 將 status 轉換為 String，再轉換為 char
        String statusStr = (String) requestData.get("status");
        
        // 確認 statusStr 不為 null，並提取第一個字符
        char status = statusStr != null && !statusStr.isEmpty() ? statusStr.charAt(0) : 'A';  // 默認 'A'
        String reason = (String) requestData.get("reason");
        String suspendedUntil = (String) requestData.get("suspendedUntil");  // 可能為 null
//        System.out.println("停權日期" + suspendedUntil);
//        System.out.println("狀態" + status);

        // 調用 Service 層來更新會員狀態
        boolean isUpdated = memberService.updateMemberStatus(memberId, status, reason, suspendedUntil);

        if (isUpdated) {
            return Result.success("會員狀態更新成功");
        } else {
            return Result.failure("會員狀態更新失敗");
        }
    }

}
