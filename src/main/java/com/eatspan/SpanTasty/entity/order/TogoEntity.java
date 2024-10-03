package com.eatspan.SpanTasty.entity.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "togo")
public class TogoEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id @Column(name = "togo_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer togoId;
	
	@Column(name = "member_id")
	private Integer memberId;     //FK(members)
	
	@Column(name = "tg_name")
	private String tgName;
	
	@Column(name = "tg_phone")
	private String tgPhone;
	
	@Column(name = "total_price")
	private Integer totalPrice;
	
	@Column(name = "togoCreateTime")
	private LocalDateTime togoCreateTime;
	
	@Column(name = "rentId")
	private Integer rentId;    //FK(rent)
	
	@Column(name = "togoStatus")
	private Integer togoStatus;
	
	@Column(name = "restaurant_id")
	private Integer restaurantId;   //FK(restaurant)
	
	@Column(name = "togo_memo")
	private String togoMemo;
	
	@Column(name = "discount_amount")
	private Integer discoutAmount;   // discount
	
	@Column(name = "final_total")
	private Integer finalTotal;   //discount
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "togo", cascade = CascadeType.ALL)
	private List<TogoItemEntity> togoItems = new ArrayList<TogoItemEntity>();
	
	
}





