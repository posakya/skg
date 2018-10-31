package com.creatu.sapnakoghar.model_class;

public class SiteResults {

    private String id;

    private String location;

    private String name;

    private String slug;

    private String code;

    private String is_publish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    @Override
    public String toString() {
        return "SiteResults{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", code='" + code + '\'' +
                ", is_publish='" + is_publish + '\'' +
                '}';
    }
}
