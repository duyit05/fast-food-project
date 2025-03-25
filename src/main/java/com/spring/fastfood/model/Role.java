package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_role")
public class Role extends AbstractEntity <Integer> {

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserHasRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "role")
    private List<RoleHasPermission> permissions = new ArrayList<>();
}
