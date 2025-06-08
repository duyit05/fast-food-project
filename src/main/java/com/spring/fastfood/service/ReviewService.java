package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.ReviewRequest;
import com.spring.fastfood.dto.response.ReviewResponse;
import lombok.*;

import java.util.List;


public interface ReviewService {
    List<ReviewResponse> getAllReview ();
    ReviewResponse addReview (ReviewRequest request);
}

