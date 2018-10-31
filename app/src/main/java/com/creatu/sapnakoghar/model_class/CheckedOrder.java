package com.creatu.sapnakoghar.model_class;

public class CheckedOrder {
    private int item_id;
    private int item_quantity;

    public CheckedOrder(int item_id, int item_quantity) {
        this.item_id = item_id;
        this.item_quantity = item_quantity;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }
}
