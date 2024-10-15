package com.eatspan.SpanTasty.entity.store;

import java.util.Set;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="product_type")
public class ProductType {

	@Id @Column(name = "product_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productTypeId;
	
	@Column(name = "product_type_name", unique = true)
	private String productTypeName;

	@JsonIgnore
	@OneToMany(fetch= FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="productType")
	private Set<Product> products;

	
	
}
