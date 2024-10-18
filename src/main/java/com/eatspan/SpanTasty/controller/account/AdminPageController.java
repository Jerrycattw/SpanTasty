package com.eatspan.SpanTasty.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
	
    @GetMapping("/admin/member-list")
    public String showMemberListPage() {
        return "spantasty//account/findMembers"; 
    }
    
    @GetMapping("/admin/admin-list")
    public String showAdminListPage() {
        return "spantasty//account/findAdmins"; 
    }
    
    @GetMapping("/admin/loginPage")
    public String showAdminloginPage() {
        return "spantasty//account/login"; 
    }
    
    @GetMapping("/admin/addAdminPage")
    public String showAddAdminPage() {
        return "spantasty//account/addAdmin"; 
    }
    
    @GetMapping("/admin/updateAdminInfoPage")
    public String showUpdateAdminInfoPage() {
        return "spantasty//account/updateAdminInfo"; 
    }
    
    @GetMapping("/admin/adminPermissionPage")
    public String showAdminPermissionPage() {
        return "spantasty//account/adminPermission"; 
    }
    
    @GetMapping("/admin/demoPage")
    public String showDemoPage() {
        return "spantasty//account/demo"; 
    }
}
