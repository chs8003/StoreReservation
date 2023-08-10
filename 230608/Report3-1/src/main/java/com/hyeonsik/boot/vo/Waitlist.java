package com.hyeonsik.boot.vo;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Waitlist {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "waitlist_id")
	private Long waitlistId; // 대기열 번호
	
	@Column(name="name")
    @NotBlank(message = "예약자 이름")
    private String name; // 예약자 이름

    @Column(name="phone_number")
    @NotBlank(message = "휴대폰 전화번호")
    private String phone_number; // 예약자 전화번호

    @Column(name = "party_size")
    @NotNull(message = "예약 인원 수")
    private Integer party_size; // 예약 인원 수
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "time_added", columnDefinition = "timestamp")
    private LocalDateTime timeAdded; // 예약 추가 시간
    
    @Column(name = "QUEUE_NUMBER")
    private Long queueNumber;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_no", referencedColumnName = "admin_no")
    private Restaurant restaurant;
    
    
    @Column(name= "user_no")
    @NotNull(message = "예약 인원 수")
    private Long userNo; // 예약 인원 수

    @NotNull(message = "가게 전화번호")
    private String storePhone; // 
    
    @NotNull(message = "예약 추가 시간")
    private String adminCafe; 
    
    @NotNull(message = "예약 추가 시간")
    private String loginType; 
    
    @Column(name ="is_blacklisted" , columnDefinition = "boolean default false")
    private boolean blacklisted;
        
    
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    @PrePersist
    public void assignQueueNumber() {
        Long currentQueueNumber = restaurant.getCurrentQueueNumber();
        this.queueNumber = currentQueueNumber + 1;
        restaurant.setCurrentQueueNumber(queueNumber);
    }

    
    
   	public void setUserPhone(String userPhone) {
   		this.phone_number = userPhone;
   	}

   	public void setUserName(String userName) {
		this.name = userName;
	}
    
   	public void setUserNo(Long userNo) {
   	    this.userNo = userNo;
   	}
   	
   	
    public Waitlist(Long queueNumber, String name, String phone_number, Integer party_size, String storePhone, String adminCafe, String loginType,
    		Long userNo, LocalDateTime time_added) {
    	this.queueNumber = queueNumber;
        this.name = name;
        this.phone_number = phone_number;
        this.party_size = party_size;
        this.storePhone = storePhone;
        this.adminCafe = adminCafe;
        this.loginType = loginType;
        this.userNo = userNo;
        this.timeAdded = time_added;
    }

    public Long getQueueNumber() {
        return this.queueNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public Integer getPartySize() {
        return this.party_size;
    }

    public String getStorePhone() {
        return this.storePhone;
    }

    public String getAdminCafe() {
        return this.adminCafe;
    }
    public String loginType() {
        return this.loginType;
    }
    
    public Integer getAdminNo() {
        return this.restaurant.getAdminNo();
    }
}