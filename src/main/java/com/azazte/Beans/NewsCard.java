package com.azazte.Beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by home on 27/06/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsCard {
    private String id;
    private String imageUrl;
    private String newsHead;
    private String newsBody;
    private String newsSourceUrl;
    private String newsSourceName;
    private String date;
    private long createdTime;
    private Long createdTimeEpoch;
    private String category;
    private String author;
    private String impactLabel;
    private String impact;
    private String sentiment;
    private Boolean isApproved;
    private Integer likes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsHead() {
        return newsHead;
    }

    public void setNewsHead(String newsHead) {
        this.newsHead = newsHead;
    }

    public String getNewsBody() {
        return newsBody;
    }

    public void setNewsBody(String newsBody) {
        this.newsBody = newsBody;
    }

    public String getNewsSourceUrl() {
        return newsSourceUrl;
    }

    public void setNewsSourceUrl(String newsSourceUrl) {
        this.newsSourceUrl = newsSourceUrl;
    }

    public String getNewsSourceName() {
        return newsSourceName;
    }

    public void setNewsSourceName(String newsSourceName) {
        this.newsSourceName = newsSourceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getImpactLabel() {
        return impactLabel;
    }

    public void setImpactLabel(String impactLabel) {
        this.impactLabel = impactLabel;
    }

    public Long getCreatedTimeEpoch() {
        return createdTimeEpoch;
    }

    public void setCreatedTimeEpoch(Long createdTimeEpoch) {
        this.createdTimeEpoch = createdTimeEpoch;
    }
}
