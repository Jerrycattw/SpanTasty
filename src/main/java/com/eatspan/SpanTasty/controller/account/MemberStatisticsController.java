package com.eatspan.SpanTasty.controller.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public Result<Map<String, Object>> getMemberCountByYearMonth(@RequestParam int year, @RequestParam int month) {
		// 調用 Service 層方法來獲取會員數
		int memberCount = memberStatisticsService.getMemberCountByYearMonth(year, month);

		// 構建返回的 Map 來包含會員數
		Map<String, Object> data = new HashMap<>();
		data.put("memberCount", memberCount);

		// 返回一個成功的 Result 物件，包含會員數的資訊
		return Result.success(data);
	}

	// 總會員數員數
	@GetMapping("/total")
	public Result<Integer> getTotalMembers() {
		Integer totalMembers = memberStatisticsService.getTotalMembers();
		return Result.success(totalMembers);

	}

	// 當月新會員
	@GetMapping("/new-this-month")
	public Result<Integer> getNewMembersThisMonth() {
		Integer newMembers = memberStatisticsService.getNewMembersThisMonth();
		return Result.success(newMembers);
	}

	// 當月活耀會員
	@GetMapping("/active-this-month")
	public Result<Integer> getActiveMembersThisMonth() {
		Integer activeMembers = memberStatisticsService.getActiveMembersThisMonth();
		return Result.success(activeMembers);
	}

	// 當月每日活耀會員數
	@GetMapping("/active-members-daily/{year}/{month}")
	public Result<List<Map<String, Object>>> getActiveMembersForMonth(@PathVariable int year, @PathVariable int month) {
		List<Map<String, Object>> activeMembersDaily = memberStatisticsService.getActiveMembersForMonth(year, month);
		return Result.success(activeMembersDaily);
	}

	// 停權會員數
	@GetMapping("/suspended")
	public Result<Integer> getSuspendedMembers() {
		Integer suspendedMembers = memberStatisticsService.getSuspendedMembers();
		return Result.success(suspendedMembers);
	}

	// 年齡分布
	@GetMapping("/age-distribution")
	public Result<Map<String, Integer>> getAgeDistribution() {
		Map<String, Integer> ageDistribution = memberStatisticsService.getMembersAgeDistribution();
		return Result.success(ageDistribution);
	}

	@GetMapping("/registrations-per-month/{year}")
	public Result<List<Map<String, Object>>> getMonthlyRegistrations(@PathVariable int year) {
		List<Map<String, Object>> monthlyRegistrations = memberStatisticsService.getMonthlyRegistrations(year);
		return Result.success(monthlyRegistrations);
	}

}
