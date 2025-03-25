package com.spring.fastfood.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_user_has_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserHasRole extends AbstractEntity <Integer> {

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id" , nullable = false)
    private Role role;


}
