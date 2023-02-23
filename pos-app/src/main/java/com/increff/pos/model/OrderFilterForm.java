package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderFilterForm {

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;
}
