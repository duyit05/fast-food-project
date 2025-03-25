package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_order_detail")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail extends AbstractEntity <Long> {

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
