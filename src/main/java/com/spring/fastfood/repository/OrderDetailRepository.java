package com.spring.fastfood.repository;

import com.spring.fastfood.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository <OrderDetail , Long> {
}
