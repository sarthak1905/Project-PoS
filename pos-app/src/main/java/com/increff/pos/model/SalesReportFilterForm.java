package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class SalesReportFilterForm {

    private String startDate;
    private String endDate;
    @Size(max = 30)
    private String brand;
    @Size(max = 30)
    private String category;

}
