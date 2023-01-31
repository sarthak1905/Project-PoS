package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDate;

@Getter
@Setter
public class SalesForm {

    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
    @NotBlank
    private String brand;
    @NotBlank
    private String category;

}
