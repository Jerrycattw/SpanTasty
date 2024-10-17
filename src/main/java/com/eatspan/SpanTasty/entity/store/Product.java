package com.eatspan.SpanTasty.entity.store;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "product")
public class Product {

		@Id @Column(name = "product_id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer productId;
		
		@Column(name = "product_name")
		private String productName;
		
		@Column(name = "product_price")
		private Integer productPrice;
		
		@Column(name = "product_picture")
		private String productPicture;
		
		@Column(name = "product_stock")
		private Integer productStock;
		
		@Column(name = "product_status")
		private Integer productStatus;
		
		@Column(name = "product_description")
		private String productDescription;
		
		
		
		@ManyToOne
		@JoinColumn(name="product_type_id")
		private ProductType productType;
		
		@JsonIgnore
	    @OneToMany(fetch= FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "product")
	    private List<ShoppingItem> items;
	
	

}
