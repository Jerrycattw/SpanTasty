package com.eatspan.SpanTasty.entity.account;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.eatspan.SpanTasty.entity.reservation.Reserve;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "member_name", length = 30)
    private String memberName;

    @Column(name = "account", length = 60, nullable = false, unique = true)
    private String account;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "phone", length = 10)
    private String phone;
    
    @Lob
    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "status", length = 1)
    private char status;

    @Column(name = "login_date")
    private LocalDateTime loginDate;

    @Column(name = "suspended_until")
    private LocalDateTime suspendedUntil;

    // 新增第三方登入欄位
    @Column(name = "provider", length = 50)
    private String provider;  // 用來儲存登入提供者名稱 (例如: google)

    @Column(name = "provider_id", length = 100)
    private String providerId;  // 用來儲存第三方提供者的唯一用戶 ID
    
    
    
    
	@JsonIgnore //該屬性不要做JSON序列化避免無線迴圈 //預設lazy
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
	private List<Reserve> reserves = new ArrayList<Reserve>();
    

    
}
