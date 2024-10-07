package com.eatspan.SpanTasty.service.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.repository.account.MemberRepository;

@Service
public class MemberStatisticsService {
	
    @Autowired
    private MemberRepository memberRepository;
    
    //每月會員註冊數
    public Map<String, Integer> getMonthlyRegistrationCounts() {
        List<Object[]> registrationData = memberRepository.findMonthlyRegistrationCounts();
        Map<String, Integer> registrationCounts = new HashMap<>();
        for (Object[] row : registrationData) {
            String month = (String) row[0];
            Integer count = ((Number) row[1]).intValue();
            registrationCounts.put(month, count);
        }
        return registrationCounts;
    }
    
    // 會員狀態統計
    public Map<String, Long> getMemberStatusCounts() {
        List<Object[]> statusData = memberRepository.findMemberStatusCounts();
        Map<String, Long> statusCounts = new HashMap<>();
        for (Object[] row : statusData) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            statusCounts.put(status, count);
        }
        return statusCounts;
    }
}
