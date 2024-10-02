package com.eatspan.SpanTasty.dto.discount;

import java.util.Date;

//DTO的介面
public interface PointMemberProjection {
	Integer getMemberId();
    String getMemberName();
    String getPhone();
    Integer getTotalPointBalance();
    Integer getExpiringPoints();
    Date getExpiryDate();
}
