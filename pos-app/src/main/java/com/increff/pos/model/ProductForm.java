package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull
    @Min(value = 0)
    private Double mrp;

}
