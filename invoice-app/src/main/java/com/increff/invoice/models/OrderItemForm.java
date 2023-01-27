package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String barcode;

    @NotBlank
    private int quantity;

    @NotBlank
    private double sellingPrice;

}
