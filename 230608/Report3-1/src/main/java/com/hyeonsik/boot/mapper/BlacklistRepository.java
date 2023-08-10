package com.hyeonsik.boot.mapper;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyeonsik.boot.vo.BlackList;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.UserVo;

public interface BlacklistRepository extends JpaRepository<BlackList, Long> {
	Optional<BlackList> findByUserVoAndRestaurant(UserVo userVo, Restaurant restaurant);
}