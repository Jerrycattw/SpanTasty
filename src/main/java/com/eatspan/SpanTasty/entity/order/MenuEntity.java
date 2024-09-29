package com.eatspan.SpanTasty.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "menu")
public class MenuEntity {
	
	@Id @Column(name = "food_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer foodId;
	
	@Column(name = "food_name")
	private String foodName;
	
	@Column(name = "food_picture")
	private String foodPicture;
	
	@Column(name = "food_price")
	private Integer foodPrice;
	
	@Column(name = "food_kind_id")
	private Integer foodKindId;
	
	@Column(name = "food_stock")
	private Integer foodStock;
	
	@Column(name = "food_description")
	private String foodDescription;
	
	@Column(name = "food_status")
	private Integer foodStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_kind_id", insertable = false, updatable = false)
	private FoodKindEntity foodKind;
}
