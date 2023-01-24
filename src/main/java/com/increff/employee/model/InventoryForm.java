package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
public class InventoryForm {

    @NotBlank
    private String barcode;

    @NotBlank
    private int quantity;
}
