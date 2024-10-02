package com.eatspan.SpanTasty.service.rental;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.repository.rental.TablewareRepository;

@Service
public class TablewareService {
	
	
	@Autowired
	private TablewareRepository tablewareRepository;
	
	
	//新增餐具
	public Tableware addTableware(Tableware tableware) {
		return tablewareRepository.save(tableware);
	}
	
	
	//更新餐具
	public Tableware updateTableware(Tableware tableware) {
		Optional<Tableware> optional = tablewareRepository.findById(tableware.getTablewareId());
		if(optional.isPresent()) {
			return tablewareRepository.save(tableware);
		}
		return null;
	}

	
	//更新餐具
	public Tableware updateTableware(Integer tablewareId, String tablewareName, Integer tablewareDeposit, String tablewareImage, String tablewareDescription, Integer tablewareStatus) {
		Optional<Tableware> optional = tablewareRepository.findById(tablewareId);
		if(optional.isPresent()) {
			Tableware tableware = optional.get();
			tableware.setTablewareName(tablewareName);
			tableware.setTablewareDeposit(tablewareDeposit);
			tableware.setTablewareImage(tablewareImage);
			tableware.setTablewareDescription(tablewareDescription);
			tableware.setTablewareStatus(tablewareStatus);
			return tableware;
		}
		return null;
	}
	
	
	//更改餐具狀態
	public Tableware updateTablewareStatus(Integer tablewareId) {
		Optional<Tableware> optional = tablewareRepository.findById(tablewareId);
		if(optional.isPresent()) {
			Tableware tableware = optional.get();
			int currentStatus = tableware.getTablewareStatus();
			int newStatus = (currentStatus == 1) ? 2 : 1;
			tableware.setTablewareStatus(newStatus);
			return tableware;
		}
		return null;
	}
	
	
	//查詢餐具(By餐具編號)
	public Tableware findTablewareById(Integer tablewareId) {
		Optional<Tableware> optional = tablewareRepository.findById(tablewareId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	//查詢所有餐具
	public List<Tableware> findAllTablewares() {
		return tablewareRepository.findAll();
	}
	

	//查詢餐具(By餐具名 描述)
	public List<Tableware> findTablewaresByKeywords(String keyword) {
		return tablewareRepository.findByKeyword(keyword);
	}
	
	
//查詢餐具編號
//	public List<Integer> findTablewareIds() {
//		return tablewareRepository.findTablwareIds();
//	}
}
