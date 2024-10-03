package com.eatspan.SpanTasty.entity.discount;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class TagId implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@GenericGenerator(name = "generator", strategy = "foreign", 
			parameters = @Parameter(name="property",value="coupon"))
	@GeneratedValue(generator = "generator")
	@Column(name = "coupon_id")
	private Integer couponId;
	
	@Column(name = "tag_name")
    private String tagName;


    // 带参构造函数
    public TagId(Integer couponId, String tagName) {
        this.couponId = couponId;
        this.tagName = tagName;
    }
    
    public TagId(String tagName) {
		super();
		this.tagName = tagName;
	}

	// Getter 和 Setter
    public Integer getCouponId() { return couponId; }
    public void setCouponId(Integer couponId) { this.couponId = couponId; }

    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagId tagId = (TagId) o;
        return couponId.equals(tagId.couponId) && tagName.equals(tagId.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponId, tagName);
    }

	@Override
	public String toString() {
		return "TagId [couponId=" + couponId + ", tagName=" + tagName + "]";
	}
}