package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(indexes ={@Index(columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "category"})}, name = "brands")
@Getter
@Setter
public class BrandPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    private String brand;

    @Column(nullable = false)
    @NotBlank
    private String category;

}
