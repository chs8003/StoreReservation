package com.hyeonsik.boot.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyeonsik.boot.vo.Favorite;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.UserVo;


public interface FavoriteRepository extends JpaRepository<Favorite,Long>{
	boolean existsByUserNoAndRestaurant(Long userNo, Restaurant restaurant);

	Favorite findByUserNoAndRestaurant(Long userNo, Restaurant restaurant);
}
