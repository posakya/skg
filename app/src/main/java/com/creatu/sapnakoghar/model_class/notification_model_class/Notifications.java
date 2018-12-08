package com.creatu.sapnakoghar.model_class.notification_model_class;

public class Notifications
{
    private Content content;

    private String id;

    private String created;

    private String notification;

    private String is_read;

    public Content getContent ()
    {
        return content;
    }

    public void setContent (Content content)
    {
        this.content = content;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCreated ()
{
    return created;
}

    public void setCreated (String created)
    {
        this.created = created;
    }

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public String getIs_read ()
    {
        return is_read;
    }

    public void setIs_read (String is_read)
    {
        this.is_read = is_read;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [content = "+content+", id = "+id+", created = "+created+", notification = "+notification+", is_read = "+is_read+"]";
    }
}
