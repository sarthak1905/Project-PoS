package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String barcode;
    private Integer quantity;
    private Double sellingPrice;

}
