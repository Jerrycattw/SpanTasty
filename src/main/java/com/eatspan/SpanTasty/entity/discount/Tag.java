package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "coupon_tag")
public class Tag implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TagId tagId;

	@Column(name = "tag_type")
	private String tagType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coupon_id", insertable = false, updatable = false)
	private Coupon coupon;
}