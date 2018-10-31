package com.creatu.sapnakoghar.model_class.bill_model_class;

public class Order_bills {

    private String id;

    private String updated_at;

    private String remark;

    private String bill_image;

    private String created_at;

    private String bill_status;

    private Site_info site_info;

    private Updated_info updated_info;

    private String order_code;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getRemark ()
    {
        return remark;
    }

    public void setRemark (String remark)
    {
        this.remark = remark;
    }

    public String getBill_image ()
    {
        return bill_image;
    }

    public void setBill_image (String bill_image)
    {
        this.bill_image = bill_image;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getBill_status ()
    {
        return bill_status;
    }

    public void setBill_status (String bill_status)
    {
        this.bill_status = bill_status;
    }

    public Site_info getSite_info ()
    {
        return site_info;
    }

    public void setSite_info (Site_info site_info)
    {
        this.site_info = site_info;
    }

    public Updated_info getUpdated_info ()
    {
        return updated_info;
    }

    public void setUpdated_info (Updated_info updated_info)
    {
        this.updated_info = updated_info;
    }

    public String getOrder_code ()
    {
        return order_code;
    }

    public void setOrder_code (String order_code)
    {
        this.order_code = order_code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", updated_at = "+updated_at+", remark = "+remark+", bill_image = "+bill_image+", created_at = "+created_at+", bill_status = "+bill_status+", site_info = "+site_info+", updated_info = "+updated_info+", order_code = "+order_code+"]";
    }

}
