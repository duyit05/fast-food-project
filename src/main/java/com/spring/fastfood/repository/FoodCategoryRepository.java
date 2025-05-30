package com.spring.fastfood.repository;

import com.spring.fastfood.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory , Long> {
    void deleteAllByFood_Id(long foodId);
}
