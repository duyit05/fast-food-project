package com.spring.fastfood.model;

import com.spring.fastfood.enums.ShipType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_ship")
@AllArgsConstructor
@NoArgsConstructor
public class Ship extends AbstractEntity <Integer> {
    @Column(name = "description")
    private String description;

    @Column(name = "ship_name")
    private String shipName;

    @Column(name = "ship_price")
    private Double shipPrice;

    @Column(name = "ship_type")
    @Enumerated(EnumType.STRING)
    private ShipType shipType;

    @OneToMany(mappedBy = "ship")
    private List<Order> orders = new ArrayList<>();
}
