package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SalesForm {

    private LocalDate startDate;
    private LocalDate endDate;
    private String brand;
    private String category;

}
