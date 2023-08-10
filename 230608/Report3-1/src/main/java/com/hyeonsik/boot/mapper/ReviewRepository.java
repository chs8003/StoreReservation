package com.hyeonsik.boot.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyeonsik.boot.vo.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	List<Review> findByAdminNo(Integer adminNo);
}
