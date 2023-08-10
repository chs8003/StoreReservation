package com.hyeonsik.boot.vo;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;


import org.locationtech.jts.geom.Point;




@Data
public class MapVo {
	 private int adminNo;
	 private String adminCafe;
	 private String guName;
	 private String addressName;
	 private Point clty_loc;
	 private String storeIntroduce;
	 private String foodType;
	 private String savedNm;
	 private LocalTime openingTime;
	 private LocalTime closingTime;
	 private int weekday;
	 private String storePhone;
	 private double latitude;
	 private double longitude;
	 private List<MenuCategory> categories;
	 private boolean isReservable;
	 private int currentqueueNumber;
	 private double avgRating;
	 private int favoriteCount;
	 private int reviewCount;
}
