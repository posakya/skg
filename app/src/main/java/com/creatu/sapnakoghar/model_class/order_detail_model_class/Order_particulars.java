package com.creatu.sapnakoghar.model_class.order_detail_model_class;

public class Order_particulars {
    private String id;

    private String price;

    private String requested_quantity;

    private String name;

    private String receive_quantity;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getRequested_quantity ()
    {
        return requested_quantity;
    }

    public void setRequested_quantity (String requested_quantity)
    {
        this.requested_quantity = requested_quantity;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getReceive_quantity ()
    {
        return receive_quantity;
    }

    public void setReceive_quantity (String receive_quantity)
    {
        this.receive_quantity = receive_quantity;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", price = "+price+", requested_quantity = "+requested_quantity+", name = "+name+", receive_quantity = "+receive_quantity+"]";
    }
}
