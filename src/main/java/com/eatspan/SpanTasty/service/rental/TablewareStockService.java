package com.eatspan.SpanTasty.service.rental;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.rental.TablewareStock;
import com.eatspan.SpanTasty.entity.rental.TablewareStock.TablewareStockId;
import com.eatspan.SpanTasty.repository.rental.TablewareStockRepository;


@Service
public class TablewareStockService {
	@Autowired
	private TablewareStockRepository tablewareStockRepository;
	
	public TablewareStock saveTablewareStock(TablewareStock tablewareStock) {
		return tablewareStockRepository.save(tablewareStock);
	}
	
	public List<TablewareStock> findAllTablewareStocks(){
		return tablewareStockRepository.findAll();
	}
	
	public TablewareStock findTablewareStockByStockId(Integer tablewareId, Integer restaurantId) {
		TablewareStock tablewareStock = tablewareStockRepository.findTablewareStocksByStockId(tablewareId, restaurantId);
		if(tablewareStock != null) {
			return tablewareStock;
		}
		return null;
	}
	
	public TablewareStock updateTablewareStock(Integer tablewareId, Integer restaurantId, Integer stock) {
		TablewareStock tablewareStock = tablewareStockRepository.findTablewareStocksByStockId(tablewareId, restaurantId);
		if(tablewareStock != null) {
			tablewareStock.setStock(stock);
			return tablewareStock;
		}
		return null;
	}
	
	public List<TablewareStock> findTablewareStocksByCriteria(Integer tablewareId, Integer restaurantId) {
		return tablewareStockRepository.findTablewareStocksByCriteria(tablewareId, restaurantId);
	}
}
