package com.eatspan.SpanTasty.repository.account;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eatspan.SpanTasty.entity.account.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    // 查詢是否存在該管理員擁有指定ID的權限
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM Permission p JOIN p.admins a " +
           "WHERE a.adminId = :adminId AND p.permissionId = :permissionId")
    boolean existsByAdminIdAndPermissionId(@Param("adminId") Integer adminId, @Param("permissionId") Integer permissionId);

    List<Permission> findByPermissionNameIn(List<String> permissionNames);
    

}
