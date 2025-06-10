package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.ReviewRequest;
import com.spring.fastfood.dto.response.ReviewResponse;

import java.util.List;


public interface ReviewService {
    List<ReviewResponse> getAllReview ();
    ReviewResponse addReview (ReviewRequest request);
    void removeReview (long reviewId);
    ReviewResponse updateReview (long reviewId, ReviewRequest request);
}

