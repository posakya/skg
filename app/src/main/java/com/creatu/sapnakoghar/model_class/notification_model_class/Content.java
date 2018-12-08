package com.creatu.sapnakoghar.model_class.notification_model_class;

public class Content
{
    private String id;

    private String type;

    private String msg;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", type = "+type+", msg = "+msg+"]";
    }
}

