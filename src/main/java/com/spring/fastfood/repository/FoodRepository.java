package com.spring.fastfood.repository;

import com.spring.fastfood.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository <Food , Long> {
}
