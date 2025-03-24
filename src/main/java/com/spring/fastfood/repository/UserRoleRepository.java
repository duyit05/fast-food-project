package com.spring.fastfood.repository;

import com.spring.fastfood.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository <UserRole, Long> {
}
