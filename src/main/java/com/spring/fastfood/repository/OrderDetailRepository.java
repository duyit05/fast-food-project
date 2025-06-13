package com.spring.fastfood.repository;

import com.spring.fastfood.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository <OrderDetail , Long> {
    @Query("SELECT od.id FROM OrderDetail od WHERE od.food.id =: foodId")
    List<OrderDetail> findByFoodExist (long foodId);
}
