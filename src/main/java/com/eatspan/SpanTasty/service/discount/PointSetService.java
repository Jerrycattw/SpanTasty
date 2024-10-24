package com.eatspan.SpanTasty.service.discount;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eatspan.SpanTasty.entity.discount.PointSet;
import com.eatspan.SpanTasty.enums.PointTextVariable;
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
	
	private String getValueFromVariable(PointSet pointSet,PointTextVariable pointTextVariable) {
		return switch (pointTextVariable) {
				case AMOUNT -> String.valueOf(pointSet.getAmountPerPoint()) ;
				case POINT ->String.valueOf(pointSet.getPointsEarned());
				case EXPIRE_DATE -> pointSet.getExpiryMonth()+"/"+pointSet.getExpiryDay();
				case BIRTH_PERIOD -> pointSet.getBirthType();
				case MULTIPLY_POINT -> String.valueOf(pointSet.getPointRatio());
				case SPACE -> "<br>";
				
		};
	}
	
	public String getPointSetText(String content) {
		PointSet pointSet = findAllPointSet();
		
		for (PointTextVariable variable : PointTextVariable.values()) {
			String value = getValueFromVariable(pointSet, variable);
			content = content.replace(variable.getVaribale(), value);
		}
		return content;
		
		
	}
	
}
