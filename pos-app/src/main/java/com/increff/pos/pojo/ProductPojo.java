package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"barcode"})}, name = "products")
@Getter
@Setter
public class ProductPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank
    private String barcode;

    @Column(nullable = false, name = "brand_category")
    private Integer brandCategory;

    @Column(nullable = false)
    @Min(value=0)
    private Double mrp;

    @Column(nullable = false)
    @NotBlank
    private String name;


}
