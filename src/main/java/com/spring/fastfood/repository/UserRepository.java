package com.spring.fastfood.repository;

import com.spring.fastfood.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,Long> {
}
