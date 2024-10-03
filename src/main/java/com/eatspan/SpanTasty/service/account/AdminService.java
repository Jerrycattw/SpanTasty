package com.eatspan.SpanTasty.service.account;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.account.Admin;
import com.eatspan.SpanTasty.repository.account.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	// 檢查是否有權限
    public boolean hasPermission(Integer adminId, Integer permissionId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return false;
        }

        // 如果是超級管理員，直接擁有所有權限
        if (admin.isSuperAdmin()) {
            return true;
        }

        // 否則檢查是否擁有具體的權限
        return permissionRepository.existsByAdminIdAndPermissionId(adminId, permissionId);
    }
	
    // 分頁查詢所有管理員，使用 Pageable 來控制分頁和排序
    public Page<Admin> findAllAdmins(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    // 根據名稱進行模糊查詢管理員
    public Page<Admin> searchAdminsByName(String adminName, Pageable pageable) {
        return adminRepository.findByAdminNameContaining(adminName, pageable);
    }

    public Admin login(String account, String password) {
        // 根據帳號查找管理員
        Admin admin = adminRepository.findByAccount(account);

        // 檢查管理員是否存在且密碼是否匹配
        if (admin != null && admin.getPassword().equals(password)) {
            return admin; 
        }

        return null; 
    }


    public boolean updateAdminAvatar(Integer adminId, byte[] avatarBytes) {
        // 根據 adminId 找到管理員
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            
            // 更新頭像
            admin.setAvatar(avatarBytes);  
            
            // 保存管理員信息
            adminRepository.save(admin);
            return true;  // 更新成功
        } else {
            return false;  // 找不到該管理員
        }
    }

}
