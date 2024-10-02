package com.eatspan.SpanTasty.service.rental;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.repository.rental.TablewareStockRepository;


@Service
public class TablewareStockService {
	
	
	@Autowired
	private TablewareStockRepository tablewareStockRepository;
	
	
	//新增餐具庫存
	public TablewareStock addStock(TablewareStock tablewareStock) {
		return tablewareStockRepository.save(tablewareStock);
	}
	
	
	//更改庫存
	public TablewareStock updateStock(TablewareStock stock) {
		TablewareStock tablewareStock = tablewareStockRepository.findById(stock.getTablewareId(), stock.getRestaurantId());
		if(tablewareStock != null) {
			return tablewareStockRepository.save(tablewareStock);
		}
		return null;
	}

	
	//更改庫存
	public TablewareStock updateStock(Integer tablewareId, Integer restaurantId, Integer stock) {
		TablewareStock tablewareStock = tablewareStockRepository.findById(tablewareId, restaurantId);
		if(tablewareStock != null) {
			tablewareStock.setStock(stock);
			return tablewareStock;
		}
		return null;
	}
	
	
	//查詢餐具(By餐具 餐廳編號(可擇一))
	public List<TablewareStock> findStocksByCriteria(Integer tablewareId, Integer restaurantId) {
		return tablewareStockRepository.findByCriteria(tablewareId, restaurantId);
	}
	
	
	//查詢所有餐具
	public List<TablewareStock> findAllStocks(){
		return tablewareStockRepository.findAll();
	}
	
	
	//查詢餐具
	public TablewareStock findStockById(Integer tablewareId, Integer restaurantId) {
		TablewareStock tablewareStock = tablewareStockRepository.findById(tablewareId, restaurantId);
		if(tablewareStock != null) {
			return tablewareStock;
		}
		return null;
	}
}
