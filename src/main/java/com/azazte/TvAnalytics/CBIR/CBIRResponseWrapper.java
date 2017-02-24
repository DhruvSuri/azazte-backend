package com.azazte.TvAnalytics.CBIR;

import java.util.List;

/**
 * Created by dhruv.suri on 20/02/17.
 */
public class CBIRResponseWrapper {
    private String id;
    private List<SimilarityResponse> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SimilarityResponse> getData() {
        return data;
    }

    public void setData(List<SimilarityResponse> data) {
        this.data = data;
    }
}

class SimilarityResponse {
    private String id;
    private String similarities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSimilarities() {
        return similarities;
    }

    public void setSimilarities(String similarities) {
        this.similarities = similarities;
    }
}
