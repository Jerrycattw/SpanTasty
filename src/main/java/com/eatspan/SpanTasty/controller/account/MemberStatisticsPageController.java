package com.eatspan.SpanTasty.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberStatisticsPageController {
	
    @GetMapping("/MemberStatistics")
    public String showMemberStatisticsPage() {
        return "/account/memberStatisticsPage"; 
    }
}
