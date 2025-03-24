package com.spring.fastfood.repository;

import com.spring.fastfood.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository <Role , Long> {
}
