package com.hyeonsik.boot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hyeonsik.boot.mapper.FavoriteRepository;
import com.hyeonsik.boot.mapper.RestaurantRepository;
import com.hyeonsik.boot.mapper.UserRepository;
import com.hyeonsik.boot.vo.BlackList;
import com.hyeonsik.boot.vo.Favorite;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.UserVo;

@Service
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository,
            RestaurantRepository restaurantRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Favorite addFavorite(Long userNo, Integer adminNo) {
        Restaurant restaurant = restaurantRepository.findByAdminNo(adminNo);     
        
        if (favoriteRepository.existsByUserNoAndRestaurant(userNo, restaurant)) {
            throw new RuntimeException("이미 등록된 매장입니다.");
        }
        
        Favorite favorite = new Favorite(userNo, restaurant);
        restaurant.setFavoriteCount(restaurant.getFavoriteCount() + 1);
        restaurantRepository.save(restaurant);
        favorite = favoriteRepository.save(favorite);
        
        return favorite;
    }

    public Favorite removeFavorite(Integer adminNo,Long userNo) {
        Restaurant restaurant = restaurantRepository.findByAdminNo(adminNo);  
       
        Favorite favorite = favoriteRepository.findByUserNoAndRestaurant(userNo, restaurant);
        if (restaurant.getFavoriteCount() > 0) {
            restaurant.setFavoriteCount(restaurant.getFavoriteCount() - 1);
            restaurantRepository.save(restaurant);
            favoriteRepository.delete(favorite);
        } else {
            throw new RuntimeException("즐겨찾기가 존재하지 않습니다.");
        }
        		         
        return favorite;
    }
    
    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }
}