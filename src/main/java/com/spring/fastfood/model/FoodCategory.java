package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_food_category")
@AllArgsConstructor
@NoArgsConstructor
public class FoodCategory extends AbstractEntity <Long>  {

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
