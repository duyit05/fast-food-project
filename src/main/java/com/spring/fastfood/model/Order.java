package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractEntity <Long>{
    @Column(name = "address_receive")
    private String addressReceive;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "ship_price")
    private Double shipPrice;

    @Column(name = "total_all_food")
    private Double totalFood;

    @Column(name ="total_price")
    private Double totalPrice;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name  = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;


}
