package com.increff.pos.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "invoice")
public class InvoicePojo extends AbstractVersionPojo {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Column(nullable = false, name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String path;
}
