package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

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

    @NotBlank
    private double mrp;

}
