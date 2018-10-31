package com.creatu.sapnakoghar.model_class.order_model_class;

public class Orders {
    private String id;

    private String updated_at;

    private String remark;

    private String created_at;

    private Site_info site_info;

    private Updated_info updated_info;

    private String order_code;

    private String order_status;
    private String order_status_name;

    public String getOrder_status_name() {
        return order_status_name;
    }

    public void setOrder_status_name(String order_status_name) {
        this.order_status_name = order_status_name;
    }

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

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
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

    public String getOrder_status ()
    {
        return order_status;
    }

    public void setOrder_status (String order_status)
    {
        this.order_status = order_status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", updated_at = "+updated_at+", remark = "+remark+", created_at = "+created_at+", site_info = "+site_info+", updated_info = "+updated_info+", order_code = "+order_code+", order_status = "+order_status+",order_status_name = "+order_status_name+"]";
    }
}
