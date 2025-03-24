package com.spring.fastfood.repository;

import com.spring.fastfood.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList , Long> {
}
