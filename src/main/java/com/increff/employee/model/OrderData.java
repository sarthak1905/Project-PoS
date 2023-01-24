package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderData {

    private int id;
    private LocalDateTime dateTime;

}
