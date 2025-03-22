package com.spring.fastfood.repository;

import com.spring.fastfood.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory , Long> {
}
