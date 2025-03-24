package com.spring.fastfood.repository;

import com.spring.fastfood.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository <Ship,Long> {
}
