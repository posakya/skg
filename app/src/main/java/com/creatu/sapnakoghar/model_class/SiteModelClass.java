package com.creatu.sapnakoghar.model_class;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SiteModelClass {
    private String response_time;

    @SerializedName("results")
    @Expose
    private List<SiteResults> results;

    public List<SiteResults> getResults() {
        return results;
    }

    public void setResults(List<SiteResults> results) {
        this.results = results;
    }

    private String status;

    public String getResponse_time ()
    {
        return response_time;
    }

    public void setResponse_time (String response_time)
    {
        this.response_time = response_time;
    }


    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response_time = "+response_time+", results = "+results+", status = "+status+"]";
    }
}
