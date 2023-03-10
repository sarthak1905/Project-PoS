package com.increff.pos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String barcode;
    @Min(value = 1)
    @NotNull
    @Max(value = 999999)
    private Integer quantity;
    @Min(value = 0)
    @NotNull
    @Max(value = 999999)
    private Double sellingPrice;

}
