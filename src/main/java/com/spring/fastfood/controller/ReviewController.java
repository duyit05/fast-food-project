package com.spring.fastfood.controller;

import com.spring.fastfood.dto.request.ReviewRequest;
import com.spring.fastfood.dto.response.DataResponse;
import com.spring.fastfood.dto.response.ReviewResponse;
import com.spring.fastfood.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/review")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    DataResponse<List<ReviewResponse>> getAllReview (){
        return new DataResponse<>(HttpStatus.OK.value(), "get all review",reviewService.getAllReview());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<ReviewResponse> addReview (@RequestBody ReviewRequest request){
        return new DataResponse<>(HttpStatus.CREATED.value(), "create review",reviewService.addReview(request));
    }

    @DeleteMapping("/delete-review/{reviewId}")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<?> deleteReview (@PathVariable long reviewId){
        reviewService.removeReview(reviewId);
        return new DataResponse<>(HttpStatus.NO_CONTENT.value(), "delete review");
    }

    @PutMapping("/update-review/{reviewId}")
    @PreAuthorize("hasAuthority('USER')")
    DataResponse<ReviewResponse> updateReview (@PathVariable long reviewId,@Valid @RequestBody ReviewRequest request){

        return new DataResponse<>(HttpStatus.ACCEPTED.value(), "update review",reviewService.updateReview(reviewId,request));
    }
}
