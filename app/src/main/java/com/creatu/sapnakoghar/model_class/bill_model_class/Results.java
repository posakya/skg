package com.creatu.sapnakoghar.model_class.bill_model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Results {

    private String last_page;

    private String total;

    private String per_page;

    @SerializedName("order_bills")
    @Expose
    private List<Order_bills> order_bills;

    public List<Order_bills> getOrder_bills() {
        return order_bills;
    }

    public void setOrder_bills(List<Order_bills> order_bills) {
        this.order_bills = order_bills;
    }

    private String current_page;

    public String getLast_page ()
    {
        return last_page;
    }

    public void setLast_page (String last_page)
    {
        this.last_page = last_page;
    }

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    public String getPer_page ()
    {
        return per_page;
    }

    public void setPer_page (String per_page)
    {
        this.per_page = per_page;
    }



    public String getCurrent_page ()
    {
        return current_page;
    }

    public void setCurrent_page (String current_page)
    {
        this.current_page = current_page;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [last_page = "+last_page+", total = "+total+", per_page = "+per_page+", order_bills = "+order_bills+", current_page = "+current_page+"]";
    }
}
