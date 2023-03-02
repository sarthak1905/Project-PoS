package com.increff.pos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class InvoiceForm {

    @NotBlank
    private Integer orderId;
    @NotBlank
    private Double orderTotal;
    @NotBlank
    private String orderDate;
    @NotBlank
    private List<OrderItemData> orderItemData;
    @NotBlank
    private String invoiceDate;
}
