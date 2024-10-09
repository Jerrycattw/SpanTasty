package com.eatspan.SpanTasty.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberStatisticsPageController {
	
    @GetMapping("/memberStatistics")
    public String showMemberStatisticsPage() {
        return "spantasty//account/memberStatisticsPage"; 
    }
}
