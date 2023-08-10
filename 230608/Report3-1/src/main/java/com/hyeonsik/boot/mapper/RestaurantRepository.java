package com.hyeonsik.boot.mapper;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hyeonsik.boot.vo.Restaurant;


public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	 Optional<Restaurant> findByAdminCafe(String adminCafe);
	 Restaurant findByAdminNo(int adminNo);
	 @Query("SELECT r FROM Restaurant r WHERE lower(r.adminCafe) LIKE %:keyword% OR lower(r.addressName) LIKE %:keyword%")
	 List<Restaurant> findByKeyword(@Param("keyword") String keyword);
	 
}