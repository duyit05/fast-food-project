package com.spring.fastfood.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends AbstractEntity{

    @Column(name = "description")
    private String description;

    @Column(name = "payment_name")
    private String paymentName;

    @OneToMany(mappedBy = "payment")
    private List<Order> orders = new ArrayList<>();
}
