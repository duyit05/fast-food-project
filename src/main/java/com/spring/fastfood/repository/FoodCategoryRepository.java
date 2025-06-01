package com.spring.fastfood.repository;

import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory , Long> {

    @Query("SELECT f FROM Category c " +
            "JOIN FoodCategory fc ON fc.category.id = c.id " +
            "JOIN Food f ON f.id = fc.food.id " +
            "WHERE c.id = :categoryId")
    List<Food> findFoodByCategoryId(@Param("categoryId") long categoryId);

}
