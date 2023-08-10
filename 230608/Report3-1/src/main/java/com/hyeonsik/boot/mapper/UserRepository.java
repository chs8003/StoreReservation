package com.hyeonsik.boot.mapper;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import com.hyeonsik.boot.vo.UserInformation;
import com.hyeonsik.boot.vo.UserVo;

public interface UserRepository extends JpaRepository<UserVo, Long> {
	UserVo findByUserNo(Long userNo);
	UserVo findByUserId(String userId);
	
}