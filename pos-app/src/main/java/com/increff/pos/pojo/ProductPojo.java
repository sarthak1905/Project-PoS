package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Table (name="products",
        indexes = {@Index(name="barcode_index", columnList = "barcode"),
                   @Index(name="brand_category_index", columnList = "brandCategory")})
@Getter
@Setter
public class ProductPojo extends AbstractVersionPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    @NotBlank
    private String barcode;

    @Column(nullable = false)
    private Integer brandCategory;

    @Column(nullable = false)
    @Min(value=0)
    private Double mrp;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String name;


}
