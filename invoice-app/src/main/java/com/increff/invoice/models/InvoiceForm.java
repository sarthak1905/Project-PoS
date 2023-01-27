package com.increff.invoice.models;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceForm {

    private OrderData orderData;
    private List<OrderItemData> orderItemData;
    private String invoiceDate;
}
