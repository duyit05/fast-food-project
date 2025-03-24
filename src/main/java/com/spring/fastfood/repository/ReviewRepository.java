package com.spring.fastfood.repository;

import com.spring.fastfood.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository <Review , Long> {
}
