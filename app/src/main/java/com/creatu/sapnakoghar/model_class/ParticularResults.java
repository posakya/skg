package com.creatu.sapnakoghar.model_class;

public class ParticularResults {

    private String id;

    private String name;

    private String slug;

    private String is_publish;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIs_publish() {
        return is_publish;
    }

    public void setIs_publish(String is_publish) {
        this.is_publish = is_publish;
    }

    @Override
    public String toString() {
        return "ParticularResults{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", is_publish='" + is_publish + '\'' +
                '}';
    }
}