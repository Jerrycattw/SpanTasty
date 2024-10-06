package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity @Table(name = "point")

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "point_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pointId;
	
	@Column(name = "member_id")
	private Integer memberId;
	
	@Column(name = "point_change")
	private Integer pointChange;
	
	@Column(name = "created_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "Asia/Taipei")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	
	@Column(name = "get_expiry_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "Asia/Taipei")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expiryDate;
	
	@Column(name = "point_usage")
	private Integer pointUsage;
	
	@Column(name = "transaction_id")
	private int transactionId;
	
	@Column(name = "transaction_type")
	private String transactionType;
	
	@ColumnTransformer(write = "NULLIF(?, '')")
	@Column(name = "transaction_description")
	private String transactionDescription;
	
	public Point(int memberID, int pointChange, Date createDate, Date expiryDate, int transactionID,
			String transactionType) {
		this.memberId = memberID;
		this.pointChange = pointChange;
		this.createDate = createDate;
		this.expiryDate = expiryDate;
		this.transactionId = transactionID;
		this.transactionType = transactionType;
	}

	@Override
	public String toString() {
		return "Point [pointId=" + pointId + ", memberId=" + memberId + ", pointChange=" + pointChange + ", createDate="
				+ createDate + ", expiryDate=" + expiryDate + ", pointUsage=" + pointUsage + ", transactionId="
				+ transactionId + ", transactionType=" + transactionType + ", transactionDescription="
				+ transactionDescription + "]";
	}

	
	public Map.Entry<Integer, Integer> pointToMapEntry() {
		return new AbstractMap.SimpleEntry<>(pointId, pointUsage);
		
	}
	
}	
	