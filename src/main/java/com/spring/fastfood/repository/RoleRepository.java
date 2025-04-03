package com.spring.fastfood.repository;

import com.spring.fastfood.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository <Role , Long> {

    @Query(value = "select r from Role r inner join UserHasRole ur on r.id = ur.user.id where ur.user.id =: userId")
    List<Role> getAllByUserId (long userId);

    Optional<Role> findByRoleName (String roleName);
}
