package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class InventoryForm {

    @NotBlank
    private String barcode;
    private Integer quantity;
}
