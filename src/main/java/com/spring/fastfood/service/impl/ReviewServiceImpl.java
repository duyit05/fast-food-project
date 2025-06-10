package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.ReviewRequest;
import com.spring.fastfood.dto.response.FoodResponse;
import com.spring.fastfood.dto.response.ReviewResponse;
import com.spring.fastfood.dto.response.UserResponse;
import com.spring.fastfood.exception.ForBiddenException;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.Review;
import com.spring.fastfood.model.User;
import com.spring.fastfood.repository.ReviewRepository;
import com.spring.fastfood.service.FoodService;
import com.spring.fastfood.service.ReviewService;
import com.spring.fastfood.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final FoodService foodService;

    @Override
    public List<ReviewResponse> getAllReview() {
        List<Review> reviews = reviewRepository.findAll();
        List<ReviewResponse> reviewList = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponse response = new ReviewResponse();
            response.setReviewId(review.getId());
            response.setComment(review.getComment());
            response.setRank(review.getRank());

            FoodResponse foodResponse = new FoodResponse();
            foodResponse.setId(review.getFood().getId());
            foodResponse.setBrand(review.getFood().getBrand());
            foodResponse.setDescription(review.getFood().getDescription());
            foodResponse.setFoodName(review.getFood().getFoodName());

            UserResponse userResponse = new UserResponse();
            userResponse.setUsername(review.getUser().getUsername());

            response.setFood(foodResponse);
            response.setUser(userResponse);
            reviewList.add(response);
        }
        return reviewList;
    }

    @Override
    public ReviewResponse addReview(ReviewRequest request) {
        User user = userService.findByUsername(userService.getContextHolder());
        Food food = foodService.getFoodById(request.getFoodId());
        Review review = Review.builder()
                .food(food)
                .user(user)
                .comment(request.getComment())
                .rank(request.getRank())
                .build();
        reviewRepository.save(review);

        return ReviewResponse.builder()
                .reviewId(review.getId())
                .comment(review.getComment())
                .rank(review.getRank())
                .build();
    }

    public Review getReviewById(long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("not found with review id:" + reviewId));
    }

    @Override
    public void removeReview(long reviewId) {
        User user = userService.findByUsername(userService.getContextHolder());
        Review review = getReviewById(reviewId);
        if (!review.getUser().equals(user)) {
            throw new ForBiddenException("you can't delete this review");
        }
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public ReviewResponse updateReview(long reviewId, ReviewRequest request) {
        User user = userService.findByUsername(userService.getContextHolder());
        Review review = getReviewById(reviewId);
        if (!review.getUser().equals(user)) {
            throw new ForBiddenException("you can't update this review");
        } else {
            review.setComment(request.getComment());
            review.setRank(request.getRank());
            reviewRepository.save(review);
        }
        log.info("review: {}" , review);
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .comment(review.getComment())
                .rank(review.getRank())
                .build();
    }
}
