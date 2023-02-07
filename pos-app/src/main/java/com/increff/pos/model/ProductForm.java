package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ProductForm {

    @NotBlank
    private String barcode;

    @NotBlank
    private String brand;

    @NotBlank
    private String category;

    @NotBlank
    private String name;

    @Min(value = 0)
    private double mrp;

}
