package com.spring.fastfood.repository;

import com.spring.fastfood.model.Order;
import com.spring.fastfood.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long> {
    List<Order> findByUser (User user);
}
