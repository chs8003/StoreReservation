package com.hyeonsik.boot.service;


import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hyeonsik.boot.mapper.BlacklistRepository;
import com.hyeonsik.boot.mapper.RestaurantRepository;
import com.hyeonsik.boot.mapper.UserRepository;
import com.hyeonsik.boot.mapper.WaitlistHistoryRepository;
import com.hyeonsik.boot.mapper.WaitlistRepository;
import com.hyeonsik.boot.vo.BlackList;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.UserVo;
import com.hyeonsik.boot.vo.UserWaitlist;
import com.hyeonsik.boot.vo.Waitlist;
import com.hyeonsik.boot.vo.WaitlistHistory;



@Service
public class WaitlistService {
    private final WaitlistRepository waitlistRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final WaitlistHistoryRepository waitlistHistoryRepository;
    private final BlacklistRepository blacklistRepository;
    private final UserService userService;
   
    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:sss");
    Date time = new Date();
    String localTime = format.format(time);
    

    
    public WaitlistService(WaitlistRepository waitlistRepository, RestaurantRepository restaurantRepository, 
    		UserRepository userRepository, WaitlistHistoryRepository waitlistHistoryRepository, BlacklistRepository blacklistRepository, UserService userService) {
        this.waitlistRepository = waitlistRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.waitlistHistoryRepository = waitlistHistoryRepository;
        this.blacklistRepository = blacklistRepository;
        this.userService= userService;
        
    }
    
    // 대기열 추가 메서드
    public Waitlist addWaitlist(Waitlist waitlist, int adminNo) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(adminNo);
        
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            UserVo userVo = userService.findByUserNo(waitlist.getUserNo());
            Optional<BlackList> blacklistData = blacklistRepository.findByUserVoAndRestaurant(userVo, restaurant);
            boolean isBlacklisted = blacklistData.isPresent();
            restaurant.addWaitlist(waitlist);  
            waitlist.setRestaurant(restaurant);
            waitlist.setBlacklisted(isBlacklisted);
            WaitlistHistory waitlistHistory = new WaitlistHistory();
            waitlistHistory.setAdminCafe(waitlist.getAdminCafe());
            waitlistHistory.setName(waitlist.getName());
            waitlistHistory.setParty_size(waitlist.getParty_size());
            waitlistHistory.setTimeAdded(waitlist.getTimeAdded());
            waitlistHistory.setUserNo(waitlist.getUserNo());
            waitlistHistory.setAdminNo(waitlist.getAdminNo());
            
            waitlistRepository.save(waitlist);
            waitlistHistoryRepository.save(waitlistHistory);
            
            return waitlist;
        } else {
            throw new RuntimeException("Restaurant not found");
        }
    }
    
    
    // 대기열 삭제 매서드
    @Transactional
    public void deleteWaitlist(Integer adminNo) {
        List<Waitlist> waitlists = waitlistRepository.findByRestaurant_AdminNoOrderByTimeAdded(adminNo);
        		  if (waitlists.isEmpty()) {
        	            throw new EntityNotFoundException();
        	        }
        Waitlist waitlist = waitlists.get(0);
        Long queueNumber = waitlist.getQueueNumber();
        waitlistRepository.delete(waitlist);
        waitlistRepository.updateQueueNumbers(queueNumber, adminNo);
        Restaurant restaurant = restaurantRepository.findById(adminNo)
                .orElseThrow(EntityNotFoundException::new);
        restaurant.setCurrentQueueNumber(restaurant.getCurrentQueueNumber() - 1);
        restaurantRepository.save(restaurant);
    }
    
 // 대기열 삭제 매서드
    @Transactional
    public void deleteWaitlist2(Long userNo, Integer adminNo) {
        Optional<Waitlist> waitlists = waitlistRepository.findByUserNoAndRestaurant_AdminNo(userNo,adminNo)
        .stream()
        .findFirst();
        		  if (waitlists.isEmpty()) {
        	            throw new EntityNotFoundException();
        	        }
        Waitlist waitlist = waitlists.get();
        Long queueNumber = waitlist.getQueueNumber();
        waitlistRepository.delete(waitlist);
        waitlistRepository.updateQueueNumbers(queueNumber, adminNo);
        Restaurant restaurant = restaurantRepository.findById(adminNo)
                .orElseThrow(EntityNotFoundException::new);
        restaurant.setCurrentQueueNumber(restaurant.getCurrentQueueNumber() - 1);
        restaurantRepository.save(restaurant);
    }

    // 손님 입장에서의 대기열 조희
    public List<Waitlist> getWaitlistByUserNo(Long userNo) {
        List<Waitlist> waitlist = waitlistRepository.findByUserNo(userNo);
        return waitlist;
      }
    
    // 가게 별 대기열 조회
    public List<UserWaitlist> getWaitlistByAdminNo(Integer adminNo) {
        List<Waitlist> waitlist = waitlistRepository.findByRestaurantAdminNo(adminNo);
        List<UserWaitlist> userWaitlists = new ArrayList<>();
        for (Waitlist w : waitlist) {
            UserWaitlist userWaitlist = new UserWaitlist(w.getUserNo(),w.getAdminNo(),w.getQueueNumber(), w.getName(),
            		                                     w.getPhone_number(), w.getParty_size(), w.getTimeAdded(), w.isBlacklisted());
            userWaitlists.add(userWaitlist);
        }
        
        return userWaitlists;
    }
    
    // 대기열 중복 방지
    public boolean isUserAlreadyInWaitlist(Long userNo, Integer adminNo) {
        Optional<Waitlist> optionalWaitlist = waitlistRepository.findByUserNoAndRestaurant_AdminNo(userNo, adminNo);
        return  optionalWaitlist.isPresent() || waitlistRepository.existsByUserNo(userNo);
    }
    
  
   
      
    // 회원가입
    public UserVo createUser(UserVo userVo) {
        String cafeName = userVo.getUserCafe();
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByAdminCafe(cafeName);
        if (optionalRestaurant.isPresent()) {
            Restaurant restaurant = optionalRestaurant.get();
            userVo.setAdmin_id(restaurant.getAdminNo());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userVo.setUserPw(passwordEncoder.encode(userVo.getPassword()));
            userVo.setUserAuth("ROLE_ADMIN");
            userVo.setAppendDate(localTime);
            userVo.setUpdateDate(localTime);
            return userRepository.save(userVo);
        } else {
            throw new RuntimeException("매장 정보를 찾을 수 없습니다.");
        }
    }
    
    //예약 ON/OFF 상태 변경
    public void toggleIsRevable(int adminNo) {
        Restaurant restaurant = restaurantRepository.findByAdminNo(adminNo);
        restaurant.setIs_reservable(!restaurant.getIs_reservable());
        restaurantRepository.save(restaurant);
    }
    
    
    // UserVo(유저정보) 아이디로 정보 찾기
    public UserVo findUserById(Long userNo) {
        return userRepository.findByUserNo(userNo);
    }
    
    public Restaurant findByAdminNo(Integer adminNo) {
    	return restaurantRepository.findByAdminNo(adminNo);
    }
    
    public List<WaitlistHistory> getWaitlistHistory(Long userNo) {
        return waitlistHistoryRepository.findByUserNo(userNo);
    }
       
    
    /*
     * OFDA 작은주파수를 잘개 잘라서 무반동파를 만듬(고속전송가능)
     대역 확산 : DSSS 직접확산(CDMA) 특징 노이즈에강하고 보안에 유리?, / 주파수 도약 대역확산(FHSS) =5개의 주파수로 확산되어 전송전력1/5로 간섭작용 특징 블루투스,지그바에서 활용 보안에강함
     확산코드의 종류: 왈시코드:하다마드 행렬을 이용-특징:높은 직교성을 가짐, 의사잡음코드 
    @Scheduled(cron =  "0 0 12 * * ?") // 매일 자정에 실행
    public void resetQueueNumber() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            restaurant.setCurrentQueueNumber(0L);
            restaurantRepository.save(restaurant);
        }
    }
    */
}