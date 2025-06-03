package com.spring.fastfood.repository;

import com.spring.fastfood.model.Cart;
import com.spring.fastfood.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser (User user);
}
