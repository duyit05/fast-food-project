package com.spring.fastfood.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tbl_image")
@AllArgsConstructor
@NoArgsConstructor
public class Image extends AbstractEntity <Long>{

    @Column(name = "data_iamge", columnDefinition = "LONGTEXT")
    private String dataImage;

    @Column(name = "image_name")
    private String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id")
    private Food food;
}
