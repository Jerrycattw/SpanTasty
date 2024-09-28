package com.eatspan.SpanTasty.entity.reservation;



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

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "restaurant_table")
public class RestaurantTable {
	
    @EmbeddedId
    private RestaurantTableId id; // 嵌入的複合主鍵

    @Column(name = "table_type_number")
    private Integer tableTypeNumber; // 桌子數量
    
    
    @ManyToOne //預設(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", insertable = false, updatable = false)
    private Restaurant restaurant; // 與 Restaurant 的關聯
 
    @ManyToOne //預設(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_type_id", insertable = false, updatable = false)
    private TableType tableType; // 與 TableType 的關聯
    
	

}
