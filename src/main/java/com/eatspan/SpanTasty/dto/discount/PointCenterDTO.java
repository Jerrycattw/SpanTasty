package com.eatspan.SpanTasty.dto.discount;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.eatspan.SpanTasty.entity.discount.Point;

import lombok.Data;

@Data
public class PointCenterDTO {

	private String transaction;
	private Long totalPoint;
	private Long totalUsedPoint;
	private String usedPercentage;
	private Long totalExpierdPoint;
	private List<?> pointBean;
	private List<PointMemberDTO> pointMembers;

	public void calculateAndSetUsedPercentage() {
		if(totalUsedPoint==null) {
			totalUsedPoint=(long) 0;
		}
		
		if (totalUsedPoint > 0) {
			double percentage = (double) totalUsedPoint / totalPoint * 100;
			DecimalFormat df = new DecimalFormat("#.00");
			
			this.usedPercentage = df.format(percentage)+"%";
		}else {
			this.usedPercentage = "-" ;
		}
	}

	public PointCenterDTO(String transaction, Long totalPoint, Long totalUsedPoint, Long totalExpierdPoint) {
		super();
		this.transaction = transaction;
		this.totalPoint = totalPoint;
		this.totalUsedPoint = totalUsedPoint;
		this.totalExpierdPoint = totalExpierdPoint;
	}

	public PointCenterDTO(Long totalPoint, Long totalUsedPoint, Long totalExpierdPoint) {
		super();
		this.totalPoint = totalPoint;
		this.totalUsedPoint = totalUsedPoint;
		this.totalExpierdPoint = totalExpierdPoint;
	}
}
