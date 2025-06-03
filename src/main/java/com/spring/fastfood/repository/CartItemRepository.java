package com.spring.fastfood.repository;

import com.spring.fastfood.model.Cart;
import com.spring.fastfood.model.CartItem;
import com.spring.fastfood.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndFood (Cart cart, Food food);
}
