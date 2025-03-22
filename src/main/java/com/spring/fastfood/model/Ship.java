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
@Table(name = "ship")
@AllArgsConstructor
@NoArgsConstructor
public class Ship extends AbstractEntity {
    @Column(name = "description")
    private String description;

    @Column(name = "ship_name")
    private String shipName;

    @Column(name = "ship_price")
    private Double shipPrice;

    @OneToMany(mappedBy = "ship")
    private List<Order> orders = new ArrayList<>();
}
