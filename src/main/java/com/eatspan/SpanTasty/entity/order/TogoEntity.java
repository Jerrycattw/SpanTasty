package com.eatspan.SpanTasty.entity.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.eatspan.SpanTasty.entity.account.Member;
import com.eatspan.SpanTasty.entity.reservation.Restaurant;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC+8")
	@Column(name = "togoCreateTime")
	private LocalDateTime togoCreateTime;
	
	@Transient
    private String formattedDate;

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
	
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
	
	@ManyToOne
	@JoinColumn(name = "member_id", insertable = false, updatable = false)
	private Member member;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "togo", cascade = CascadeType.ALL)
	private List<TogoItemEntity> togoItems = new ArrayList<TogoItemEntity>();
	
	@ManyToOne
	@JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
	private Restaurant restaurant;
	
	@PrePersist
	public void defaultData() {
		if (togoCreateTime == null) {
			togoCreateTime = LocalDateTime.now();
		}
		if(this.togoStatus == null) {
			this.togoStatus = 1;
		}
	}
	
}





