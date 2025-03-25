package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_category")
@AllArgsConstructor
@NoArgsConstructor
public class Category extends AbstractEntity <Integer>{

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL)
    private List<FoodCategory> foodCategories = new ArrayList<>();
}
