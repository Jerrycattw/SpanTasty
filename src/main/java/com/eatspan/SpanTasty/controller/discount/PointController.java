package com.eatspan.SpanTasty.controller.discount;

import java.util.List;
import java.util.Map;

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
	
	
	//點數中心-----------------------------
	@GetMapping("/pointCenter")
	public String pointCenter() {
		return "discount/point/pointCenter";
	}
	
	@GetMapping("/pointCenter/show")
	@ResponseBody
	public Map<String, Object> pointCenterShow(){
		return pointService.pointCenterResult();	
	}
	
	
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
	
//	//Home查詢-------------------------
//	@GetMapping//  /point在類上
//	public String findAllPointMembers(Model model) {
//		List<PointMemberDTO> pointMembers = pointService.getAllPointMember();
//		System.out.println(pointMembers);
//		model.addAttribute("pointMembers", pointMembers);
//		return "discount/point/pointMembers";	
//	}
	
//	// search-------------------------
//	@GetMapping("/search")
//	public String searchPoint(@RequestParam(value = "q", required = false) String keyWord, Model model) {
//		List<PointMemberDTO> pointMembers = pointService.searchPointMember(keyWord);
//		model.addAttribute("pointMembers", pointMembers);
//		model.addAttribute("keyWord", keyWord);
//		return "discount/point/pointMembers";
//	}
	
	// search-------------------------
	@GetMapping("/api/search")
	@ResponseBody
	public List<PointMemberDTO> searchPointApi(@RequestParam(value = "q", required = false) String keyWord, Model model) {
		return pointService.searchPointMember(keyWord);

	}
	
	
	// 批次新增-------------------------
//	@PostMapping("/batchAdd")
//	@ResponseBody
//	public String batchInsertPoint(@RequestBody List<String> memberIds, Model model) {
//		System.out.println(memberIds);
//		String message = pointService.printMessage(memberIds);
//
//		model.addAttribute("point_memberIds", memberIds);
//		model.addAttribute("point_message", message);
//
//		return "success";
//	}
	
	@PostMapping("/api/batchAdd")
	@ResponseBody
	public String batchInsertPointApi(@RequestBody List<String> memberIds, Model model) {
		System.out.println(memberIds);
		model.addAttribute("point_memberIds", memberIds);
		return pointService.printMessage(memberIds);
	}
	
	
//	@GetMapping("/batchAdd/members")
//	public String batchInsertPoint() {
//		return "discount/point/batchAddPoint";
//	}
	
	
	@PostMapping("/batchAdd/members")
	public String batchInsertExcute(@SessionAttribute("point_memberIds") List<String> memberIds, Point pointBean) {
		pointService.insertBatchRecord(memberIds, pointBean);
		return "redirect:/point/pointCenter";
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
	
//	// 修改-------------------------
//	@GetMapping("/member/{memberId}/point/{pointId}")
//	public String getPoint(@PathVariable Integer pointId, Model model) {
//		Point point = pointService.getOneRecord(pointId);
//		model.addAttribute("point", point);
//		return "discount/point/updatePoint";
//	}
	
	@GetMapping("/api/member/{memberId}/point/{pointId}")
	@ResponseBody
	public Point getPointApi(@PathVariable Integer pointId, Model model) {
		return pointService.getOneRecord(pointId);		
	}
	

	@PutMapping("/member/{memberId}/point/{pointId}")
	public String updatePoint(@PathVariable Integer memberId, Point pointBean) {
		System.out.println("touch");
		System.out.println(pointBean);
		pointService.updatePoint(pointBean);
		return "redirect:/point/member/"+memberId;
	}
	
	// 新增-------------------------
//	@GetMapping("/member/{memberId}/point")
//	public String insertPoint(@PathVariable Integer memberId,Model model) {
//		model.addAttribute("memberId", memberId);
//		return "discount/point/addPoint";
//	}
	
	@PostMapping("/member/point/new")
	public String insertPointNew(@RequestParam Integer memberId,@RequestParam String plusOrMinus,Point pointBean) {
		switch (plusOrMinus) {
			case "plus" -> pointBean.setPointChange(pointBean.getPointChange());
			case "minus" -> pointBean.setPointChange(pointBean.getPointChange()*-1);
		};
		pointBean.setMemberId(memberId);
		

		pointService.insertOneRecord(pointBean);
		return "redirect:/point/pointCenter";
	}
	
	
	@PostMapping("/member/{memberId}/point")
	public String insertPoint( @PathVariable Integer memberId,@RequestParam String plusOrMinus,Point pointBean) throws Exception {
		switch (plusOrMinus) {
			case "plus" -> pointBean.setPointChange(pointBean.getPointChange());
			case "minus" -> pointBean.setPointChange(pointBean.getPointChange()*-1);
		};
		pointBean.setMemberId(memberId);
		System.out.println(pointBean);
		pointService.usePoint(pointBean.getPointChange(), pointBean.getMemberId());
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
