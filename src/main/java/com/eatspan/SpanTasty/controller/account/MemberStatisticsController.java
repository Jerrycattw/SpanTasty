package com.eatspan.SpanTasty.controller.account;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatspan.SpanTasty.service.account.MemberStatisticsService;
import com.eatspan.SpanTasty.utils.account.Result;

@RestController
@RequestMapping("/api/statistics")
public class MemberStatisticsController {
	
	
    @Autowired
    private MemberStatisticsService memberStatisticsService;
    
    @GetMapping("/registrationCounts")
    public Result<Map<String, Integer>> getRegistrationCounts(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        Map<String, Integer> registrationCounts = memberStatisticsService.getRegistrationCounts(year, month);
        return Result.success(registrationCounts);
    }

    @GetMapping("/statusCounts")
    public Result<Map<String, Long>> getStatusCounts() {
        Map<String, Long> data = memberStatisticsService.getMemberStatusCounts();
        return Result.success(data);
    }
    
    @GetMapping("/count")
    public Result<Map<String, Object>> getMemberCountByYearMonth(
        @RequestParam int year, 
        @RequestParam int month
    ) {
        // 調用 Service 層方法來獲取會員數
        int memberCount = memberStatisticsService.getMemberCountByYearMonth(year, month);
        
        // 構建返回的 Map 來包含會員數
        Map<String, Object> data = new HashMap<>();
        data.put("memberCount", memberCount);
        
        // 返回一個成功的 Result 物件，包含會員數的資訊
        return Result.success(data);
    }
}