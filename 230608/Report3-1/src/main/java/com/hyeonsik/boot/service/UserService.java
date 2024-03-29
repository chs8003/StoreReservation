package com.hyeonsik.boot.service;

import com.hyeonsik.boot.mapper.MemberRepository;
import com.hyeonsik.boot.mapper.UserMapper;
import com.hyeonsik.boot.mapper.UserRepository;
import com.hyeonsik.boot.vo.MapVo;
import com.hyeonsik.boot.vo.Member;
import com.hyeonsik.boot.vo.MenuVo;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.UserInformation;
import com.hyeonsik.boot.vo.UserVo;
import lombok.RequiredArgsConstructor;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
	
	  @Autowired
	  SqlSession sqlSession;
	  
	  @Autowired
	  UserRepository userRepository;
	  
	  @Autowired
	  MemberRepository memberRepository;
	  
	  
	
	 
    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
    Date time = new Date();
    String localTime = format.format(time);
    
    private final UserMapper userMapper;

  
    
    @Transactional
    public void joinUser(UserVo userVo){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userVo.setUserPw(passwordEncoder.encode(userVo.getPassword()));
        userVo.setUserAuth("ROLE_USER");
        userVo.setAppendDate(localTime);
        userVo.setUpdateDate(localTime);
        userMapper.saveUser(userVo);
    }
    
    
    
    @Transactional
    public void joinAdmin(UserVo userVo){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userVo.setUserPw(passwordEncoder.encode(userVo.getPassword()));
        userVo.setUserAuth("ROLE_ADMIN");
        userVo.setAppendDate(localTime);
        userVo.setUpdateDate(localTime);
        userMapper.saveUser(userVo);
    }
    
    
    
    

    @Override
    public UserVo loadUserByUsername(String userId) throws UsernameNotFoundException {
        //여기서 받은 유저 패스워드와 비교하여 로그인 인증
        UserVo userVo = userMapper.getUserAccount(userId);
        if (userVo == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        return userVo;
    }
    

    public int idOverlap(String userId) throws Exception {
    	int count = userMapper.idOverlap(userId);
        return count;
    }
    
    public MapVo storefound(int adminNo) {
    	return userMapper.Menufound(adminNo);
    }
    
    
    public MapVo locationfound(int adminNo) {
    	return userMapper.Cafefound(adminNo);
    }
    public List<MapVo> found(double latitude, double longitude) {
    	return userMapper.found(latitude,longitude);
    }
    
    
   
    public int upImg(String savedNm ,String userId) throws Exception  {   
    	return userMapper.updateImg(savedNm, userId);
     
    }



    public UserVo findByUserNo(Long userNo) {
        return userRepository.findByUserNo(userNo);
    }
    
    public UserVo findByUserId(String userId) {
    	
        return userRepository.findByUserId(userId);
    }
    
    public List<MapVo> selectAll() throws Exception{
       return userMapper.Mapfound();
    }
    
 
    public Member check2(Member member) {
        String email = member.getEmail();
        Long id = member.getId();
        String name = member.getName();
        
        
        return member;
       
    }
    
}