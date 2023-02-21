package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import static com.increff.pos.pojo.GeneratorTable.POS_PRODUCT_SEQ;
import static com.increff.pos.pojo.GeneratorTable.POS_TABLE_NAME;

@Entity
@Table (name="products",
        indexes = {@Index(name="barcode_index", columnList = "barcode"),
                   @Index(name="brand_category_index", columnList = "brand_category")})
@Getter
@Setter
public class ProductPojo extends AbstractVersionPojo {

    @Id
    @TableGenerator(name = POS_PRODUCT_SEQ, pkColumnValue = POS_PRODUCT_SEQ, table = POS_TABLE_NAME)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_PRODUCT_SEQ)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    @NotBlank
    private String barcode;

    @Column(nullable = false, name = "brand_category")
    private Integer brandCategory;

    @Column(nullable = false)
    @Min(value=0)
    private Double mrp;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String name;


}
