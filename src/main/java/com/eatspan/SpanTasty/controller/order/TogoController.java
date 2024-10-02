package com.eatspan.SpanTasty.controller.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@PostMapping("/togo")
	public ResponseEntity<List<TogoEntity>> addTogo(@RequestParam TogoEntity newTogo) {
		togoService.addTogo(newTogo);
		return ResponseEntity.ok(togoService.getAllTogo());
	}
	
	//有updateTogo:更新訂單資訊，無updateTogo:刪除，更改togoStatus=3
	@PutMapping("/togo/{togoId}")
	public ResponseEntity<TogoEntity> updateTogoById(@PathVariable Integer togoId, @RequestBody TogoEntity updateTogo) {
		TogoEntity togo = togoService.getTogoById(togoId);
		if (togo == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			if (updateTogo.getTogoId() != null) {
				return ResponseEntity.ok(togoService.updateTogoById(togoId, updateTogo));
			}
			return ResponseEntity.ok(togoService.deleteTogoById(togoId));
		}
	}
	
}





