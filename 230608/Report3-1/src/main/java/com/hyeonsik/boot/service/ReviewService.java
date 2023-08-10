package com.hyeonsik.boot.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeonsik.boot.mapper.RestaurantRepository;
import com.hyeonsik.boot.mapper.ReviewRepository;
import com.hyeonsik.boot.mapper.UserRepository;
import com.hyeonsik.boot.vo.Restaurant;
import com.hyeonsik.boot.vo.Review;
import com.hyeonsik.boot.vo.UserVo;

@Service
public class ReviewService {
	
	  private final ReviewRepository reviewRepository;
	    private final UserRepository userRepository;
	    private final RestaurantRepository restaurantRepository;

	    @Autowired
	    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
	        this.reviewRepository = reviewRepository;
	        this.userRepository = userRepository;
	        this.restaurantRepository = restaurantRepository;
	    }
	
	public Review createReview(double rating, String content, int adminNo, String imageNamed, String name) {
        Review review = new Review();
        review.setRating(rating);
        review.setContent(content);
        review.setAdminNo(adminNo);
        review.setImageNamed(imageNamed);
        review.setName(name);
        review.setDate(LocalDate.now());
        
        Review savedReview = reviewRepository.save(review);
        updateRestaurantAverageRating(savedReview.getAdminNo());

        return savedReview;
    }
	
	

	private void updateRestaurantAverageRating(Integer adminNo) {
    List<Review> reviews = reviewRepository.findByAdminNo(adminNo);
    if (!reviews.isEmpty()) {
    	double averageRating = Math.round(reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0) * 10) / 10.0;

        Restaurant restaurant = restaurantRepository.findByAdminNo(adminNo);
        if (restaurant != null) {
            restaurant.setAverageRating(averageRating);
            restaurant.setReviewCount(reviews.size());
            restaurantRepository.save(restaurant);
        }
    }
}
	public List<Review> findReview(Integer adminNo) {
		List<Review> review = reviewRepository.findByAdminNo(adminNo);
		return review;
	}
	

}
