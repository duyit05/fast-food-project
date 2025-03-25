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
@Table(name = "tbl_permission")
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends AbstractEntity <Integer> {

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "permission")
    private List<RoleHasPermission> roleHasPermissons = new ArrayList<>();
}
