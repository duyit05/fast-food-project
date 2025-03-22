package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "food_category")
@AllArgsConstructor
@NoArgsConstructor
public class FoodCategory extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
