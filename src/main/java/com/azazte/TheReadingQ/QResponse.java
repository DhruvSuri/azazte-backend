package com.azazte.TheReadingQ;

/**
 * Created by home on 27/01/17.
 */
public class QResponse {
    private int numberOfItemsRemaining;
    private String url;
    private String responseText;

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public int getNumberOfItemsRemaining() {
        return numberOfItemsRemaining;
    }

    public void setNumberOfItemsRemaining(int numberOfItemsRemaining) {
        this.numberOfItemsRemaining = numberOfItemsRemaining;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
