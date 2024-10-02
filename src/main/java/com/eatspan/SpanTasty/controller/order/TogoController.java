package com.eatspan.SpanTasty.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eatspan.SpanTasty.entity.order.TogoEntity;
import com.eatspan.SpanTasty.service.order.TogoService;

@RestController
public class TogoController {
	
	@Autowired
	private TogoService togoService;
	
	//查詢
	// 有參數：模糊查詢，無參數：查詢全部
	@GetMapping("/togo")
	public ResponseEntity<List<TogoEntity>> getTogo(
			@RequestParam(required = false) Integer memberId,
			@RequestParam(required = false) String tgPhone) {
		//有輸入memberId
		if (memberId != null) {
			List<TogoEntity> togo = togoService.getTogoByMemberId(memberId);
			// 檢查memberId有沒有訂單資料，沒有:404，有:回傳訂單
			if (togo.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok(togo);
		}
		//有輸入tgPhone
		if (tgPhone != null) {
			List<TogoEntity> togo = togoService.getTogoByPhone(tgPhone);
			// 檢查tgPhone有沒有訂單資料，沒有:404，有:回傳訂單
			if (togo.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok(togo);
		}
		return ResponseEntity.ok(togoService.getAllTogo());
	}
	
	@GetMapping("/togo/{foodId}")
	public ResponseEntity<TogoEntity> getTogoById(@PathVariable Integer togoId) {
		return ResponseEntity.ok(togoService.getTogoById(togoId));
	}
	
	
}



