package com.spring.fastfood.repository;

import com.spring.fastfood.model.UserHasRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository <UserHasRole, Long> {
}
