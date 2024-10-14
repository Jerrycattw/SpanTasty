package com.eatspan.SpanTasty.controller.account;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eatspan.SpanTasty.service.account.GoogleOauthService;
import com.eatspan.SpanTasty.utils.account.Result;

@Controller
public class GoogleLoginController {
	
	@Autowired
    private GoogleOauthService googleOauthService;

//     Google 登入請求
    @GetMapping("/login/google")
    @ResponseBody
    public Result<String> login() throws GeneralSecurityException, IOException {
        String googleAuthUrl = googleOauthService.login();
        System.out.println(googleAuthUrl);
        return Result.success("重定向到 Google 登入頁面", googleAuthUrl);
    }

    // 處理 Google OAuth 的回調
    @GetMapping("/oauth2/callback/google")
    public String callback(@RequestParam String code, Model model) {
        try {
            String token = googleOauthService.handleCallback(code);  // 獲取 JWT token
            System.out.println("token :"+token);
            model.addAttribute("token", token);  // 將 JWT Token 傳給前端

            // 返回到 callback.html
            return "starcups/account/callback";
        } catch (Exception e) {
            return "redirect:/error";  // 如果有錯誤，跳轉到錯誤頁面
        }
    }
}
