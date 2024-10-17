package com.eatspan.SpanTasty.service.rental;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.rental.RentItem;
import com.eatspan.SpanTasty.entity.rental.Tableware;
import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.repository.rental.TablewareRepository;
import com.eatspan.SpanTasty.repository.rental.TablewareStockRepository;

@Service
public class TablewareService {
	
	
	@Autowired
	private TablewareRepository tablewareRepository;
	@Autowired
	private TablewareStockRepository tablewareStockRepository;
	
	// 新增餐具
	public Tableware addTableware(Tableware tableware) {
		return tablewareRepository.save(tableware);
	}
	
	
	// 更新餐具
	public Tableware updateTableware(Tableware tableware) {
		Optional<Tableware> optional = tablewareRepository.findById(tableware.getTablewareId());
		if(optional.isPresent()) {
			return tablewareRepository.save(tableware);
		}
		return null;
	}

	
	// 更新餐具
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
	
	
	// 更改餐具狀態
	public Tableware updateTablewareStatus(Integer tablewareId) {
		Optional<Tableware> optional = tablewareRepository.findById(tablewareId);
		if(optional.isPresent()) {
			Tableware tableware = optional.get();
			int currentStatus = tableware.getTablewareStatus();
			int newStatus = (currentStatus == 1) ? 2 : 1;
			tableware.setTablewareStatus(newStatus);
			tablewareRepository.save(tableware);
			return tableware;
		}
		return null;
	}
	
	
	// 查詢餐具(By餐具編號)
	public Tableware findTablewareById(Integer tablewareId) {
		Optional<Tableware> optional = tablewareRepository.findById(tablewareId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	
	// 查詢所有餐具
	public List<Tableware> findAllTablewares() {
		return tablewareRepository.findAll();
	}
	

	// 查詢餐具(By餐具名 描述)
	public List<Tableware> findTablewaresByKeywords(String keyword) {
		return tablewareRepository.findByKeyword(keyword);
	}
	
	
	// 查詢所有餐具(Page)
	public Page<Tableware> findAllTablewarePages(Integer page){
		Pageable pageable = PageRequest.of(page-1, 10, Sort.Direction.ASC, "tablewareId");
 		return tablewareRepository.findAll(pageable);
	}
	
	
	//
	public List<Tableware> findByRestaurantId(Integer restaurantId) {
	    List<TablewareStock> stockItems = tablewareStockRepository.findByRestaurantId(restaurantId);
	    return stockItems.stream()
	            .map(TablewareStock::getTableware)
	            .filter(tableware -> tableware.getTablewareStatus() != 2) // 過濾掉 tablewareStatus == 2 的餐具
	            .collect(Collectors.toList());
	}
	
	
	//
	public Integer countTableware() {
		return tablewareRepository.countTableware();
	}
	
	
	//
	public Integer findTablewareDeposit(Integer tablewareId) {
		return tablewareRepository.findTablewareDeposit(tablewareId);
	}
}
