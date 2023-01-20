package com.increff.employee.model;

public class InventoryForm {

    private String barcode;
    // TODO Should always be boxed type. Use @NotNull if required
    // TODO Also, for quantity, can have @Min(0)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

}
