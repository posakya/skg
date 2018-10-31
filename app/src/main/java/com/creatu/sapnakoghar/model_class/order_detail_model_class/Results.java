package com.creatu.sapnakoghar.model_class.order_detail_model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {

    @SerializedName("order_particulars")
    @Expose
    private List<Order_particulars> order_particulars;

    public List<Order_particulars> getOrder_particulars() {
        return order_particulars;
    }

    public void setOrder_particulars(List<Order_particulars> order_particulars) {
        this.order_particulars = order_particulars;
    }

    private Order_detail order_detail;

    @SerializedName("order_venders")
    @Expose
    private List<Order_venders> order_venders;

    public List<Order_venders> getOrder_venders() {
        return order_venders;
    }

    public void setOrder_venders(List<Order_venders> order_venders) {
        this.order_venders = order_venders;
    }

    public Order_detail getOrder_detail ()
    {
        return order_detail;
    }

    public void setOrder_detail (Order_detail order_detail)
    {
        this.order_detail = order_detail;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [order_particulars = "+order_particulars+", order_detail = "+order_detail+", order_venders = "+order_venders+"]";
    }
}
