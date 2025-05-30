package com.spring.fastfood.repository;

import com.spring.fastfood.model.Food;
import com.spring.fastfood.model.User;
import com.spring.fastfood.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList , Long> {
    Optional<WishList> findByUserAndFood (User user, Food food);
    List<WishList> findByUser (User user);
}
