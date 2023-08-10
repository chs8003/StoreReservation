package com.hyeonsik.boot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeonsik.boot.mapper.BlacklistRepository;
import com.hyeonsik.boot.vo.BlackList;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.UserVo;

@Service
public class BlacklistService {

    private final BlacklistRepository blacklistRepository;
    private final UserService userService;
    private final WaitlistService waitlistService;

    @Autowired
    public BlacklistService(BlacklistRepository blacklistRepository, UserService userService, WaitlistService waitlistService) {
        this.blacklistRepository = blacklistRepository;
        this.userService = userService;
        this.waitlistService = waitlistService;
    }

    public BlackList addToBlacklist(Integer adminNo ,Long userNo, String content) {
    	UserVo userVo = userService.findByUserNo(userNo);
        Restaurant restaurant = waitlistService.findByAdminNo(adminNo);

        BlackList blacklist = new BlackList();
        blacklist.setUserVo(userVo);
        blacklist.setRestaurant(restaurant);
        blacklist.setContent(content);

        return blacklistRepository.save(blacklist);
    }
   
    
    public BlackList deleteBlacklist(Integer adminNo,Long userNo) {
        UserVo userVo = userService.findByUserNo(userNo);
        Restaurant restaurant = waitlistService.findByAdminNo(adminNo);

        BlackList blacklist = blacklistRepository.findByUserVoAndRestaurant(userVo, restaurant)
                .orElse(null);

        if (blacklist != null) {
            blacklistRepository.delete(blacklist);
        }
        
        return blacklist;
    }
}