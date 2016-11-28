package com.azazte.Beans;

/**
 * Created by home on 27/10/16.
 */
public class Summary {
    private String title;
    private String summary;
    private String image;

    public Summary(String title, String summary, String image) {
        this.title = title;
        this.summary = summary;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
