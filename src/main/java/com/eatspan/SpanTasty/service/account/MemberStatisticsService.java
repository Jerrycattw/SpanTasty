package com.eatspan.SpanTasty.service.account;

import java.time.LocalDate;
import java.util.ArrayList;
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

	// 每月會員註冊數
	public Map<String, Integer> getRegistrationCounts(Integer year, Integer month) {
		List<Member> members = memberRepository.findAll(); // 可以根據 year, month 查詢特定條件
		Map<String, Integer> registrationCounts = new HashMap<>();

		for (Member member : members) {
			LocalDate registerDate = member.getRegisterDate();
			if ((year == null || registerDate.getYear() == year)
					&& (month == null || registerDate.getMonthValue() == month)) {
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

	public Integer getTotalMembers() {
		return memberRepository.countTotalMembers();
	}

	public Integer getNewMembersThisMonth() {
		return memberRepository.countMembersRegisteredThisMonth();
	}

	// 當月登入會員數查詢
	public Integer getActiveMembersThisMonth() {
		return memberRepository.countActiveMembersThisMonth();
	}

	// 停權會員數查詢
	public Integer getSuspendedMembers() {
		return memberRepository.countSuspendedAndInactiveMembers();
	}

	// 查詢會員年齡分布
	public Map<String, Integer> getMembersAgeDistribution() {
		return memberRepository.countMembersByAgeGroup();
	}

	public List<Map<String, Object>> getActiveMembersForMonth(int year, int month) {
		// 從資料庫中查詢指定年份和月份的每日活躍會員數，並按登入日期排序
		List<Object[]> results = memberRepository.findActiveMembersByLoginDateForMonth(year, month);

		List<Map<String, Object>> dailyActiveMembersList = new ArrayList<>();

		// 將每一天的數據存入 Map，並按日期排序
		for (Object[] result : results) {
			Map<String, Object> dayData = new HashMap<>();
			dayData.put("date", result[0]); // 登入日期
			dayData.put("activeMembers", result[1]); // 活躍會員數
			dailyActiveMembersList.add(dayData);
		}

		return dailyActiveMembersList; // 返回包含每日數據的列表
	}

	// 每月的會員註冊數據
	public List<Map<String, Object>> getMonthlyRegistrations(int year) {
		return memberRepository.findMonthlyRegistrationsByYear(year);
	}

}
