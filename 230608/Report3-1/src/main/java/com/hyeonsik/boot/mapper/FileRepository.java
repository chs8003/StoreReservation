package com.hyeonsik.boot.mapper;

import java.nio.file.Files;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyeonsik.boot.vo.ImageVo;

public interface FileRepository extends JpaRepository<ImageVo,Long>{
	Optional<ImageVo> findById(Long fileId);


}
