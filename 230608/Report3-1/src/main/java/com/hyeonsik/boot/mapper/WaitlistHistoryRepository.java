package com.hyeonsik.boot.mapper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.hyeonsik.boot.vo.WaitlistHistory;

public interface WaitlistHistoryRepository extends JpaRepository<WaitlistHistory, Long> {
	List<WaitlistHistory> findByUserNo(Long userNo);
}
