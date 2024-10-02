package com.eatspan.SpanTasty.service.reservation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eatspan.SpanTasty.entity.reservation.TableType;
import com.eatspan.SpanTasty.repository.reservation.TableTypeRepository;

@Service
public class TableTypeService {
	
	@Autowired
	private TableTypeRepository tableTypeRepository;
	
	
	
	
	// 新增桌位種類
	public TableType addTableType(TableType tableType) {
		return tableTypeRepository.save(tableType);
	}
	
	
	// 刪除桌位種類
	public void deleteTableType(String tableTypeId) {
		tableTypeRepository.deleteById(tableTypeId);
	}
	
	
	// 更新桌位種類
	public TableType updateTableType(TableType tableType) {
		Optional<TableType> optional = tableTypeRepository.findById(tableType.getTableTypeId());
		if(optional.isPresent()) {
			return tableTypeRepository.save(tableType);
		}
		return null;
	}
	
	
	// 查詢桌位種類byId
	public TableType findTableTypeById(String tableTypeId) {
		return tableTypeRepository.findById(tableTypeId).orElse(null);
	}

	
	// 查詢所有桌位種類
	public List<TableType> findAllTableType() {
		return tableTypeRepository.findAll();
	}
	
	
	

}
