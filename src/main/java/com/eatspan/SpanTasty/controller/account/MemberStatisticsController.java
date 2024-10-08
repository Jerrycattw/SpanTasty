package com.eatspan.SpanTasty.controller.account;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eatspan.SpanTasty.service.account.MemberStatisticsService;
import com.eatspan.SpanTasty.utils.account.Result;

@RestController
@RequestMapping("/api/statistics")
public class MemberStatisticsController {
	
	
    @Autowired
    private MemberStatisticsService memberStatisticsService;

    @GetMapping("/registrationCounts")
    public Result<Map<String, Integer>> getRegistrationCounts() {
        Map<String, Integer> data = memberStatisticsService.getMonthlyRegistrationCounts();
        return Result.success(data);
    }

    @GetMapping("/statusCounts")
    public Result<Map<String, Long>> getStatusCounts() {
        Map<String, Long> data = memberStatisticsService.getMemberStatusCounts();
        return Result.success(data);
    }
}
