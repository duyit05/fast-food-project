package com.spring.fastfood.repository;

import com.spring.fastfood.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository <Image , Long> {
}
