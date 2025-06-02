package com.spring.fastfood.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends AbstractEntity<Long>{

    @OneToOne
    @JoinColumn(name = "user_id", unique = true,nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

}
