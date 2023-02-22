package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.*;

@Getter
@Setter
public class ProductForm {

    @NotBlank
    @Size(min = 1, max = 10)
    private String barcode;

    @NotBlank
    @Size(min = 1, max = 30)
    private String brand;

    @NotBlank
    @Size(min = 1, max = 30)
    private String category;

    @NotBlank
    @Size(min = 1, max = 30)
    private String name;

    @NotNull
    @Min(value = 0)
    @Max(value = 999999)
    private Double mrp;
}
