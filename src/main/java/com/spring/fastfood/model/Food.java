package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "food")
@AllArgsConstructor
@NoArgsConstructor
public class Food extends AbstractEntity{

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "brand")
    private String brand;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "description")
    private String description;

    @Column(name = "lasted_price")
    private Double lastedPrice;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "food",cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<FoodCategory> foodCategories = new ArrayList<>();

    @OneToMany(mappedBy = "food" , cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();

    @OneToMany(mappedBy = "food" , cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
