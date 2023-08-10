package com.hyeonsik.boot.vo;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.locationtech.jts.geom.Point;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;




@Data
@Entity
@NoArgsConstructor	
	@Table(name = "file")
	public class ImageVo {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name ="file_id")
	    private Long fileId;

	    @Column(name = "org_nm")
	    private String orgNm;

	    @Column(name = "saved_nm")
	    private String savedNm;

	    @Column(name = "saved_path")
	    private String savedPath;

}
