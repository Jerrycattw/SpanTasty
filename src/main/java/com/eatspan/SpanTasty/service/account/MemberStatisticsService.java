package com.eatspan.SpanTasty.service.account;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.repository.account.MemberRepository;

@Service
public class MemberStatisticsService {
	
    @Autowired
    private MemberRepository memberRepository;
    
    //每月會員註冊數
    public Map<String, Integer> getRegistrationCounts(Integer year, Integer month) {
        List<Member> members = memberRepository.findAll(); // 可以根據 year, month 查詢特定條件
        Map<String, Integer> registrationCounts = new HashMap<>();
        
        for (Member member : members) {
            LocalDate registerDate = member.getRegisterDate();
            if ((year == null || registerDate.getYear() == year) &&
                (month == null || registerDate.getMonthValue() == month)) {
                String key = registerDate.getYear() + "-" + String.format("%02d", registerDate.getMonthValue());
                registrationCounts.put(key, registrationCounts.getOrDefault(key, 0) + 1);
            }
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
    
    // 計算特定年份和月份的會員數量
    public int getMemberCountByYearMonth(int year, int month) {
        // 將年份和月份組合成 'yyyy-MM' 格式
        String yearMonth = String.format("%04d-%02d", year, month);
        return memberRepository.countMembersByYearMonth(yearMonth);
    }
}
