package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_cart_item")
@AllArgsConstructor
@NoArgsConstructor
public class CartItem extends AbstractEntity<Long>{

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_at_add_time", nullable = false)
    private Double priceAtAddTime;

    @Column(name = "note")
    private String note;

    @Column(name = "total_price")
    private double totalPrice;
}
