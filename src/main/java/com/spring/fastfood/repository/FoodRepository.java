package com.spring.fastfood.repository;

import com.spring.fastfood.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository <Food , Long> {
    @Query("SELECT f FROM Food f WHERE LOWER(f.foodName) LIKE %:keyword%")
    Page<Food> searchByKeyWord (String keyword, Pageable pageable);
}
