package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InventoryPojo {

    // use access modifier -> Make the fields private
    @Id
    int id;
    int quantity;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
