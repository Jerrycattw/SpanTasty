package com.eatspan.SpanTasty.entity.store;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.eatspan.SpanTasty.entity.account.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="shopping_order")
public class ShoppingOrder {

	@Id @Column(name = "shopping_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shoppingId;
    

//	@Temporal(TemporalType.TIMESTAMP) LocalDateTime有就不要了
	@Column(name = "shopping_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "UTC+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 檢查進來的時間，並做格式化
    private LocalDateTime shoppingDate;
	
	@Column(name = "shopping_total")
    private Integer shoppingTotal;
    
	@Column(name = "member_id")
    private Integer memberId;
    
	@Column(name = "shopping_status")
    private Integer shoppingStatus;
    
	@Column(name = "shopping_memo")
    private String shoppingMemo;
	
	@ManyToOne 
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    private Member member;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "shoppingOrder")
    private List<ShoppingItem> items;
	
	public String getFormattedShoppingDate() {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    return shoppingDate != null ? shoppingDate.format(formatter) : null;
	}
}
