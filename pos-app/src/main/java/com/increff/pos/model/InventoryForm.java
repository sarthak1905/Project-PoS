package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class InventoryForm {

    @NotBlank
    private String barcode;
    @Max(value = 999999)
    @Min(value = 0)
    private Integer quantity;
}
