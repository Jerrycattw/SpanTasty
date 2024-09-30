package com.eatspan.SpanTasty.entity.order;

import java.io.Serializable;
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
@Table(name = "food_kind")
public class FoodKindEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id @Column(name = "food_kind_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer foodKindId;
	
	@Column(name = "food_kind_name")
	private String foodKindName;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "foodKind")
	private List<MenuEntity> foods = new ArrayList<MenuEntity>();
	
}
