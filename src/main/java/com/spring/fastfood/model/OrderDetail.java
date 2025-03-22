package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "order_detail")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail extends AbstractEntity {

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "price")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
