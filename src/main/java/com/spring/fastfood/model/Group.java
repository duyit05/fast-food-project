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
@Table(name = "tbl_group")
public class Group extends AbstractEntity<Integer>{

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "description")
    private String description;

    @OneToOne
    private Role role;

    @OneToMany(mappedBy = "group")
    private List<GroupHasUser> groupHasUser = new ArrayList<>();
}
