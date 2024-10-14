package com.eatspan.SpanTasty.service.account;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.dto.account.AdminDto;
import com.eatspan.SpanTasty.entity.account.Admin;
import com.eatspan.SpanTasty.entity.account.Permission;
import com.eatspan.SpanTasty.repository.account.AdminRepository;
import com.eatspan.SpanTasty.repository.account.PermissionRepository;

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

	// 登入
	public Admin login(String account, String password) {
		// 根據帳號查找管理員
		Admin admin = adminRepository.findByAccount(account);

		// 檢查管理員是否存在且密碼是否匹配
		if (admin != null && admin.getPassword().equals(password)) {
			return admin;
		}

		return null;
	}

	// 更新會員頭像
	public boolean updateAdminAvatar(Integer adminId, byte[] avatarBytes) {
		// 根據 adminId 找到管理員
		Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
		if (optionalAdmin.isPresent()) {
			Admin admin = optionalAdmin.get();

			// 更新頭像
			admin.setAvatar(avatarBytes);

			// 保存管理員信息
			adminRepository.save(admin);
			return true; // 更新成功
		} else {
			return false; // 找不到該管理員
		}
	}

	// 獲得目前最後的adminId
	public int getNextAdminId() {
		Integer maxId = adminRepository.findMaxAdminId();
		return (maxId != null) ? maxId + 1 : 1;
	}

	// 新增管理員
	public boolean addAdmin(Admin admin) {
		try {
			adminRepository.save(admin);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 更新指定管理員的權限
	public boolean updatePermissions(Integer adminId, List<String> permissionNames) {
	    Optional<Admin> optionalAdmin = adminRepository.findById(adminId);

	    if (optionalAdmin.isPresent()) {
	        Admin admin = optionalAdmin.get();

	        // 根據權限名稱查找對應的權限
	        Set<Permission> newPermissions = new HashSet<>(permissionRepository.findByPermissionNameIn(permissionNames));
	        System.out.println("Loaded permissions: " + newPermissions);

	        // 清除舊的權限
	        admin.getPermissions().clear();

	        // 添加新的權限
	        admin.getPermissions().addAll(newPermissions);

	        // 保存管理員
	        adminRepository.save(admin);
	        return true;
	    } else {
	        return false;
	    }
	}
	
	// 更新管理員的權限 用於新增管理員
	@Transactional
	public boolean updateAdminPermissions(Integer adminId, List<String> permissions) {
		// 找到管理員
		Admin admin = adminRepository.findById(adminId).orElse(null);
		if (admin == null) {
			return false;
		}

		// 根據傳入的權限名稱列表找到對應的權限實體
		List<Permission> permissionList = permissionRepository.findByPermissionNameIn(permissions);
		if (permissionList == null || permissionList.isEmpty()) {
			return false; // 如果找不到任何對應的權限，返回 false
		}

		// 設置管理員的權限
		admin.setPermissions(new HashSet<>(permissionList));

		// 保存更新後的管理員
		adminRepository.save(admin);
		return true;
	}
	
	//更新管理員狀態
	public boolean updateAdminStatus(Integer adminId, char status) {
		Admin admin = adminRepository.findById(adminId).orElse(null);
		if(admin == null) {
			return false;			
		}
		admin.setStatus(status);
		adminRepository.save(admin);
		return true;
	}
	
	//更新管理員基本資訊
	public boolean updateAdminProfile(Admin admin, String newAdminName, String newPassword) {
	    try {
	        if (newAdminName != null && !newAdminName.trim().isEmpty()) {
	            admin.setAdminName(newAdminName);
	        }
	        if (newPassword != null && !newPassword.trim().isEmpty()) {
	            admin.setPassword(newPassword);
	        }
	        // 設置首次登入為 0，表示已經完成首次資料修改
	        admin.setFirstLogin(0);
	        adminRepository.save(admin);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	// 移除管理員頭像
    public boolean removeAdminAvatar(Integer adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setAvatar(null); // 將頭像設置為 null
            adminRepository.save(admin); // 保存更新後的管理員資料
            return true;
        }

        return false;
    }
    
    // 重設管理員的基本資料
    public boolean resetAdmin(Integer adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            String defaultPassword = "admin" + adminId;
            String defaultAccount = "admin" + adminId;
            String defaultName = "Admin" + adminId;
            admin.setPassword(defaultPassword);
            admin.setAccount(defaultAccount);
            admin.setAdminName(defaultName);
            admin.setAvatar(null);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }
    
    //重設管理員密碼
    public boolean resetPassword(Integer adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            String defaultPassword = "1234"; // 預設密碼
            admin.setPassword(defaultPassword);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }
    	
    // 根據權限查詢管理員
    public Page<Admin> findAdminsByPermissions(List<String> permissions, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return adminRepository.findAdminsByPermissions(permissions, pageRequest);
    }
    
    // 查詢沒有任何權限的管理員
    public Page<Admin> findAdminsWithNoPermissions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminRepository.findAllByPermissionsIsEmpty(pageable);
    }

	public Admin findById(Integer adminId) {
		return adminRepository.findById(adminId).orElse(null);
	}
	
	// 分頁查詢所有管理員，使用 Pageable 來控制分頁和排序
	public Page<Admin> findAllAdmins(Pageable pageable) {
		return adminRepository.findAll(pageable);
	}

	// 根據名稱進行模糊查詢管理員
	public Page<Admin> searchAdminsByName(String adminName, Pageable pageable) {
		return adminRepository.findByAdminNameContaining(adminName, pageable);
	}
	
	// 根據帳號進行模糊查詢管理員
	public Page<Admin> searchAdminsByAccount(String account, Pageable pageable) {
	    return adminRepository.findByAccountContainingIgnoreCase(account, pageable);
	}
	 

}
