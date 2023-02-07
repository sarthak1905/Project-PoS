package com.increff.pos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String barcode;
    @Min(value = 1)
    private int quantity;
    @Min(value = 0)
    private double sellingPrice;

}
