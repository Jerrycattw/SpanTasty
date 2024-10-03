package com.eatspan.SpanTasty.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
	
    @GetMapping("/admin/member-list")
    public String showMemberListPage() {
        return "/account/findMembers"; 
    }
    
    @GetMapping("/admin/admin-list")
    public String showAdminListPage() {
        return "/account/findAdmins"; 
    }
    
    @GetMapping("/admin/loginPage")
    public String showAdminloginPage() {
        return "/account/login"; 
    }

}
