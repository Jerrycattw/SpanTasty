package com.eatspan.SpanTasty.entity.rental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "tableware")
@NoArgsConstructor
@Getter
@Setter
public class Tableware implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id@Column(name = "tableware_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tablewareId; 
	@Column(name = "tableware_name")
	private String tablewareName; 
	@Column(name = "tableware_deposit")
	private int tablewareDeposit;
	@Column(name = "tableware_image")
	private String tablewareImage; 
	@Column(name = "tableware_description")
	private String tablewareDescription;
	@Column(name = "tableware_status")
	private int tablewareStatus;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "tableware")
	private List<TablewareStock> tablewareStocks = new ArrayList<TablewareStock>();
}
