package com.eatspan.SpanTasty.service.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.repository.account.MemberRepository;
import com.eatspan.SpanTasty.utils.account.JwtUtil;

@Service
public class GoogleOauthService {
	
    @Autowired
    private MemberRepository memberRepository;
    
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUrl;
    
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String authUrl;

    private RestTemplate restTemplate = new RestTemplate();

    // 步驟 1: 生成 Google 授權 URL，並引導用戶進行 Google OAuth 認證
    public String login() {
        String authorizationUrl = authUrl
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=code"
            + "&scope=profile email"
            + "&access_type=offline";
        
        return authorizationUrl;
    }

    // 步驟 2 & 3: Google 回調，處理授權碼並交換訪問令牌
    public String handleCallback(String authorizationCode) throws Exception {
        // 交換授權碼為訪問令牌
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Google Token 交換失敗");
        }

        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");

        // 使用訪問令牌請求 Google 用戶信息
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);
        HttpEntity<String> userInfoRequest = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);

        if (userInfoResponse.getStatusCode() != HttpStatus.OK) {
            throw new Exception("獲取用戶信息失敗");
        }

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String email = (String) userInfo.get("email");
        String googleId = (String) userInfo.get("sub");  // Google 的唯一用戶 ID
        String name = (String) userInfo.get("name");

        // 檢查用戶是否已經存在
        Member existingMember = memberRepository.findByProviderId(googleId);

        if (existingMember == null) {
            // 如果用戶不存在，創建一個新的會員
            Member newMember = new Member();
            newMember.setAccount(email);  // 使用 email 作為賬號
            newMember.setEmail(email);
            newMember.setMemberName(name);
            newMember.setProvider("Google");
            newMember.setProviderId(googleId);
            newMember.setRegisterDate(LocalDate.now());

            // 設置一個默認密碼，並加密存儲
            String defaultPassword = passwordEncoder.encode("default_password");
            newMember.setPassword(defaultPassword);

            // 保存新會員
            memberRepository.save(newMember);
            existingMember = newMember;
        } 

        // 更新會員的最後登入時間
        existingMember.setLoginDate(LocalDateTime.now());
        memberRepository.save(existingMember);  // 保存變更

        // 生成 JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", existingMember.getMemberId());
        claims.put("email", existingMember.getEmail());

        String jwtToken = JwtUtil.genToken(claims);
        		//generateToken(claims, existingMember.getAccount());

        return jwtToken;  // 返回生成的 JWT token
    }	
}
