package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Modifying;

@Getter
@Setter
@Builder
@Entity
@Table(name = "review")
@AllArgsConstructor
@NoArgsConstructor
public class Review extends AbstractEntity{

    @Column(name = "comment")
    private String comment;

    @Column(name = "ranks")
    private Double rank;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
