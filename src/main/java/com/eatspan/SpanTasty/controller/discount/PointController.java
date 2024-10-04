package com.eatspan.SpanTasty.controller.discount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.eatspan.SpanTasty.dto.discount.PointMemberDTO;
import com.eatspan.SpanTasty.entity.discount.Point;
import com.eatspan.SpanTasty.entity.discount.PointSet;
import com.eatspan.SpanTasty.service.discount.PointService;
import com.eatspan.SpanTasty.service.discount.PointSetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/point")
@SessionAttributes(names = { "point_pointMember", "point_memberIds", "point_message" })
public class PointController {
	
	@Autowired
	private PointSetService pointSetService;
	
	@Autowired
	private PointService pointService;
	
	
	//點數設定-----------------------------
	@GetMapping("/pointSet")
	public String pointSet(Model model) {
		PointSet pointSet = pointSetService.findAllPointSet();
		model.addAttribute("pointSet", pointSet);
		return "discount/point/pointSet";
	}
	
	@PutMapping("/pointSet/post")
	public String updatePointSet(PointSet pointSet) {
		pointSet.setPointSetName("general");
		pointSetService.updatePointSet(pointSet);
		return "redirect:/point/pointSet";
	}
	
	//Home查詢-------------------------
	@GetMapping//  /point在類上
	public String findAllPointMembers(Model model) {
		List<PointMemberDTO> pointMembers = pointService.getAllPointMember();
		System.out.println(pointMembers);
		model.addAttribute("pointMembers", pointMembers);
		return "discount/point/pointMembers";	
	}
	
	// search-------------------------
	@GetMapping("/search")
	public String searchPoint(@RequestParam(value = "q", required = false) String keyWord, Model model) {
		List<PointMemberDTO> pointMembers = pointService.searchPointMember(keyWord);
		model.addAttribute("pointMembers", pointMembers);
		model.addAttribute("keyWord", keyWord);
		return "discount/point/pointMembers";
	}
	
	// 批次新增-------------------------
	@PostMapping("/batchAdd")
	@ResponseBody
	public String batchInsertPoint(@RequestBody List<String> memberIds, Model model) {
		System.out.println("1111111111111");
		String message = pointService.printMessage(memberIds);

		model.addAttribute("point_memberIds", memberIds);
		model.addAttribute("point_message", message);

		return "success";
	}
	@GetMapping("/batchAdd/members")
	public String batchInsertPoint() {
		System.out.println("2222222222222222");
		return "discount/point/batchAddPoint";
	}
	
	
	@PostMapping("/batchAdd/members")
	public String batchInsertExcute(@SessionAttribute("point_memberIds") List<String> memberIds, Point pointBean) {
		System.out.println("333333333333333333");
		pointService.insertBatchRecord(memberIds, pointBean);
		return "redirect:/point";
	}
	// 點擊檢視-------------------------
	@GetMapping("/member/{memberId}")
	public String getPointsMember(@PathVariable Integer memberId, Model model) {
		model.addAttribute("point_pointMember", memberId);//
		PointMemberDTO pointMember = pointService.getPointMember(memberId);
		List<Point> pointsById = pointService.getAllRecord(memberId);
		model.addAttribute("pointMember", pointMember);
		model.addAttribute("pointsById", pointsById);
		return "discount/point/showPoint";
	}
	
	// 修改-------------------------
	@GetMapping("/member/{memberId}/point/{pointId}")
	public String getPoint(@PathVariable Integer pointId, Model model) {
		Point point = pointService.getOneRecord(pointId);
		model.addAttribute("point", point);
		return "discount/point/updatePoint";
	}

	@PutMapping("/member/{memberId}/point/{pointId}")
	public String updatePoint(@PathVariable Integer memberId, Point pointBean) {
		System.out.println(pointBean);
		pointService.updatePoint(pointBean);
		return "redirect:/point/member/"+memberId;
	}
	
	// 新增-------------------------
	@GetMapping("/member/{memberId}/point")
	public String insertPoint(@PathVariable Integer memberId,Model model) {
		model.addAttribute("memberId", memberId);
		return "discount/point/addPoint";
	}
	
	
	@PostMapping("/member/{memberId}/point")
	public String insertPoint( @PathVariable Integer memberId,Point pointBean) {
		pointBean.setMemberId(memberId);
		pointService.insertOneRecord(pointBean);
		return "redirect:/point/member/"+memberId;
	}
	
	// 刪除-------------------------
	@DeleteMapping("/member/{memberId}/point/{pointId}")
	@ResponseBody
	public ResponseEntity<?> deletePoint(@PathVariable Integer memberId,@PathVariable Integer pointId) {
		System.out.println("touch");
		pointService.deleteOneRecord(pointId);
		return ResponseEntity.ok("刪除成功!");
	}
	
	
	
}
