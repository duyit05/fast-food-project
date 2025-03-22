package com.spring.fastfood.repository;

import com.spring.fastfood.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository <Image , Long> {
}
