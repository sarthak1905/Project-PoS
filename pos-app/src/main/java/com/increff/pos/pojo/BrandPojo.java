package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static com.increff.pos.pojo.GeneratorTable.POS_BRAND_SEQ;
import static com.increff.pos.pojo.GeneratorTable.POS_TABLE_NAME;

@Entity
@Table(indexes ={@Index(name="brand_category_index", columnList = "brand,category")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"brand", "category"})}, name = "brands")
@Getter
@Setter
public class BrandPojo extends AbstractVersionPojo {

    @Id
    @TableGenerator(name = POS_BRAND_SEQ, pkColumnValue = POS_BRAND_SEQ, table = POS_TABLE_NAME)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = POS_BRAND_SEQ)
    private Integer id;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String brand;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String category;

}
