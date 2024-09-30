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
	
	public Tableware saveTableware(Tableware tableware) {
		return tablewareRepository.save(tableware);
	}
	
	public Tableware findTablewareById(Integer tablewareId) {
		Optional<Tableware> optional = tablewareRepository.findById(tablewareId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<Tableware> findAllTablewares() {
		return tablewareRepository.findAll();
	}
	
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
	
	public Tableware updateTablewareById(Integer tablewareId, String tablewareName, Integer tablewareDeposit, String tablewareImage, String tablewareDescription, Integer tablewareStatus) {
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
	
	public List<Integer> findTablewareIds() {
		return tablewareRepository.findTablwareIds();
	}
	
	public List<Tableware> findTablewaresByKeywords(String keyword) {
		return tablewareRepository.findTablewaresByKeywords(keyword);
	}
}
