package com.eatspan.SpanTasty.service.discount;

import java.beans.Transient;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.discount.PointSet;
import com.eatspan.SpanTasty.repository.discount.PointSetRepository;

@Service
public class PointSetService {
	
	@Autowired
	private PointSetRepository pointSetRepo;
	
	
	public PointSet findAllPointSet() {
		//目前只有"general"一個設定
		List<PointSet> result = pointSetRepo.findAll();
		
		if(result.isEmpty()) {
			PointSet pointSet=new PointSet();
			pointSet.setPointSetName("general");
			pointSet.setAmountPerPoint(100);
			pointSet.setPointsEarned(1);
			pointSet.setPointRatio(2);
			pointSet.setExpiryMonth("1");
			pointSet.setExpiryDay("1");
			pointSet.setBirthType("當月");
			pointSet.setSetDescription("test");
			pointSet.setIsExpiry("isExpiry");
			
			return pointSetRepo.save(pointSet); 
		}
		return result.get(0);
		
	}
	
	@Transactional
	public PointSet updatePointSet(PointSet pointSet) {
		PointSet result = pointSetRepo.findByPointSetName("general");
		
		if(result != null) {
			return pointSetRepo.save(pointSet);
		}
		return null;
	}
	
	
}
