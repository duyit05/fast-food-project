package com.spring.fastfood.repository;

import com.spring.fastfood.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
        SELECT u FROM User u 
        WHERE LOWER(u.phoneNumber) IN :keywords 
           OR LOWER(u.email) IN :keywords
    """)
    Page<User> searchByKeywords(List<String> keywords, Pageable pageable);
}
