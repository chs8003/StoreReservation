package com.hyeonsik.boot.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hyeonsik.boot.mapper.FileRepository;
import com.hyeonsik.boot.vo.ImageVo;

@Service
public class UploadFile {
	
	@Autowired
	private FileRepository fileRepository;

	public String saveFile(MultipartFile file) throws IOException {
	// 파일 저장
		 String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		    String savedName = UUID.randomUUID().toString() + "." + file.getOriginalFilename().split("\\.")[1];
		    File folder = new File("/root/resource/" + date);
		    if (!folder.exists()) {
		        folder.mkdirs(); // 폴더가 존재하지 않으면 생성합니다.
		    }
	File dest = new File(folder.getPath() + "/" + savedName);
    file.transferTo(dest);

	// 데이터베이스에 파일 정보 저장
	ImageVo imageVo = new ImageVo();
	imageVo.setOrgNm(file.getOriginalFilename());
	imageVo.setSavedNm(savedName);
	imageVo.setSavedPath(dest.getAbsolutePath());
	fileRepository.save(imageVo);
	 return date + "/" + savedName;

}
}