package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(indexes ={@Index(name="brand_category_index", columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "category"})}, name = "brands")
@Getter
@Setter
public class BrandPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String brand;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String category;

}
