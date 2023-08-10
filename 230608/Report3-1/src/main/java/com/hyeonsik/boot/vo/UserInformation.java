package com.hyeonsik.boot.vo;

import lombok.Data;


@Data
public class UserInformation {
	
	private String userId;
	private String userName;
	private String userPhone;
	private String userCafe;
	private String savedNm;
	
	
	public UserInformation(String userId, String userName, String userPhone, String userCafe , String savedNm) {
		this.userId = userId;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userCafe = userCafe;
		this.savedNm = savedNm;
		
		
		
	}
	

}
