package com.spring.fastfood.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class FoodRequest {
    @Min(value = 1, message = "stock must be greater than 0")
    private Integer stock;
    @NotBlank(message = "brand must not be blank")
    private String brand;
    @NotBlank(message = "food name must not be blank")
    private String foodName;
    @NotBlank(message = "description must not be blank")
    private String description;
    private Double lastedPrice;
    @Min(value = 10, message = "price must be greater than 10")
    private Double price;
    private MultipartFile image;
    private List<Long> categoryId;
    private List<MultipartFile> images;
}
