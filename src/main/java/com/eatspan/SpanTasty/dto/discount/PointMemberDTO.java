package com.eatspan.SpanTasty.dto.discount;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointMemberDTO {

	private Integer memberId;
	private String memberName;
	private String phone;
	private Integer totalPointBalance=0;
	private Integer expiringPoints=0;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date expiryDate;

	public PointMemberDTO(Integer memberId, String memberName, String phone, Integer totalPointBalance,
			Integer expiringPoints, Date expiryDate) {
		this.memberId = memberId;
		this.memberName = memberName;
		this.phone = phone;
		this.totalPointBalance = totalPointBalance;
		this.expiringPoints = expiringPoints;
		this.expiryDate = expiryDate;
	}

	@Override
	public String toString() {
		return "PointMemberDTO [memberId=" + memberId + ", memberName=" + memberName + ", phone=" + phone
				+ ", totalPointBalance=" + totalPointBalance + ", expiringPoints=" + expiringPoints + ", expiryDate="
				+ expiryDate + "]";
	}

}
