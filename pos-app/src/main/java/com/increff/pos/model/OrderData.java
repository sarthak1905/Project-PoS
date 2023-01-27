package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderData {

    private Integer id;
    private LocalDateTime dateTime;
    private boolean invoiceStatus;
    private LocalDateTime invoiceDate;
    private Double orderTotal;

}
