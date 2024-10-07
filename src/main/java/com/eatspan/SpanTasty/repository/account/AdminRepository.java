package com.eatspan.SpanTasty.repository.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.account.Admin;


@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
    // 根據名稱進行模糊查詢
    Page<Admin> findByAdminNameContaining(String adminName, Pageable pageable);
    
    // 帳號搜尋
	Admin findByAccount(String account);
	
	// 
    @Query("SELECT MAX(a.adminId) FROM Admin a")
    Integer findMaxAdminId();

    Page<Admin> findByPermissions_PermissionIdIn(List<Integer> permissionIds, Pageable pageable);
    Page<Admin> findByPermissionsIsEmpty(Pageable pageable);
    
    @Query("SELECT a FROM Admin a WHERE " +
            "(SELECT COUNT(p) FROM a.permissions p WHERE p.permissionName IN :permissions) = :#{#permissions.size()} " +
            "AND (COALESCE(:permissions, null) IS NULL OR SIZE(a.permissions) > 0)")
     Page<Admin> findAdminsByPermissions(List<String> permissions, Pageable pageable);

	Page<Admin> findAllByPermissionsIsEmpty(Pageable pageable);
	
	

}
