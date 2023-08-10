package com.hyeonsik.boot.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.Waitlist;



public interface WaitlistRepository extends JpaRepository<Waitlist, Long>, WaitlistRepositoryCustom {
	 List<Waitlist> findByRestaurant_AdminNoOrderByTimeAdded(Integer adminNo);
	 List<Waitlist> findByUserNo(Long userNo);
	 List<Waitlist> findByRestaurantAdminNo(Integer adminNo);
	 Optional<Waitlist> findByUserNoAndRestaurant_AdminNo(Long userNo, Integer adminNo);
	 @Query("SELECT w FROM Waitlist w JOIN FETCH w.restaurant r LEFT JOIN BlackList bl ON "
	 		+ "w.userNo = bl.userVo.userNo AND r.adminNo = bl.restaurant.adminNo WHERE r.adminNo = :adminNo")
	 List<Waitlist> findWaitlistByRestaurantAdminNoWithBlacklistCheck(@Param("adminNo") Integer adminNo);
	 
	 boolean existsByUserNo(Long userNo);
}