package com.hyeonsik.boot.vo;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "cafe")
public class Restaurant {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "admin_no")
	 private Integer adminNo; // 관리자 번호
	 
	 @Column(name ="admin_cafe")
	 private String adminCafe;
	 
	 @Column(name ="address_name")
	 private String addressName;

    private Boolean is_reservable;  // 예약 가능 여부
        
    @Column(name = "CURRENT_QUEUE_NUMBER", nullable = false, columnDefinition = "int default 0")
    private Long currentQueueNumber;
    
    @Column(name = "favorite_count",columnDefinition = "int default 0")
    private int favoriteCount;
    
    @Column(name = "review_count" ,columnDefinition = "int default 0")
    private int reviewCount;

    @Column(name = "avg_rating" ,columnDefinition = "double default 0.0")
    private double averageRating;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Waitlist> waitlist;
    
    
    
    public void addWaitlist(Waitlist waitlist) {
        if (!this.is_reservable) {
            throw new RuntimeException("예약 불가능 상태");
        }
        waitlist.setRestaurant(this);
        this.waitlist.add(waitlist);
    }
   
}