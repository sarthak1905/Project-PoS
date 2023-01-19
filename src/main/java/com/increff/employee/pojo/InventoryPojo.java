package com.increff.employee.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InventoryPojo {

    @Id
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Integer quantity;

}
