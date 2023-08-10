package com.hyeonsik.boot.vo;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class UserWaitlist {

	private Long userNo;
	private Integer adminNo;
	private String name;
    private String phone_number;
    private Long queueNumber;
    private int party_size;
    private LocalDateTime timeAdded;
    private boolean blacklisted;
    
    public UserWaitlist(Long userNo, Integer adminNo, Long queueNumber,String name, String phone_number, int party_size, LocalDateTime timeAdded, boolean blacklisted) {
    	this.userNo = userNo;
    	this.adminNo = adminNo;
    	this.queueNumber= queueNumber;
        this.name = name;
        this.phone_number = phone_number;
        this.party_size = party_size;
        this.timeAdded = timeAdded;
        this.blacklisted = blacklisted;
    }
}