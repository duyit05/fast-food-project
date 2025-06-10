package com.spring.fastfood.repository;

import com.spring.fastfood.model.Review;
import com.spring.fastfood.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository <Review , Long> {
    @Query("SELECT r.id FROM Review r WHERE r.id =: reviewId AND r.user.id =: user")
    void deleteReviewByUser (long reviewId, User user);

}
