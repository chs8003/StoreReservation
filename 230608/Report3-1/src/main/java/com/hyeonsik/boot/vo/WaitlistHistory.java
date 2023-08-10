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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
public class WaitlistHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long HistoryId; // 대기열 번호
	
	@Column(name="name")
    @NotBlank(message = "예약자 이름")
    private String name; // 예약자 이름

    @Column(name="phone_number")
    @NotBlank(message = "식당 이름")
    private String adminCafe; 

    @Column(name = "party_size")
    @NotNull(message = "예약 인원 수")
    private Integer party_size; // 예약 인원 수
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "time_added", columnDefinition = "timestamp")
    private LocalDateTime timeAdded; // 예약 추가 시간
    
    @Column(name= "admin_no")
    @NotNull(message = "예약 인원 수")
    private Integer adminNo; // 예약 인원 수
    
    @Column(name= "user_no")
    @NotNull(message = "예약 인원 수")
    private Long userNo; // 예약 인원 수

}
