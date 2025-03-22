package com.spring.fastfood.repository;

import com.spring.fastfood.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository <Payment , Long> {
}
