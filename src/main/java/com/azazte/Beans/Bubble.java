package com.azazte.Beans;

import java.util.List;

/**
 * Created by home on 13/08/16.
 */
public class Bubble {
    private String id;
    private String storyId;
    private String answer;  // This is channelDataListString.Due to some historic reasons it is named as answer
    private List<ChannelData> channelDataList;
    private long createdTime;

    public Bubble() {
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChannelData> getChannelDataList() {
        return channelDataList;
    }

    public void setChannelDataList(List<ChannelData> channelDataList) {
        this.channelDataList = channelDataList;
    }
}
