package com.azazte.Bubble;

import com.azazte.Beans.Bubble;
import com.azazte.Beans.ChannelData;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by home on 13/08/16.
 */
public class BubbleService {
    private static BubbleService bubbleService;
    Logger logger = LoggerFactory.getLogger(BubbleService.class);

    public static BubbleService getInstance() {
        if (bubbleService == null) {
            bubbleService = new BubbleService();
        }
        return bubbleService;
    }

    public List<Bubble> fetchBubbles(String storyId, Integer skip, Integer limit) {
        Query query = new Query();
        if (limit == null) {
            limit = 10;
        }
        if (skip == null) {
            skip = 0;
        }
        query.limit(limit);
        query.skip(skip);
        query.addCriteria(Criteria.where("storyId").is(storyId));
        query.with(new Sort(Sort.Direction.DESC, "createdTime"));
        return MongoFactory.getMongoTemplate().find(query, Bubble.class);
    }

    public void saveBubble(Bubble bubble) {
        if (bubble.getStoryId() == null) {
            throw new RuntimeException("Bubble without a story ID...wtf ?");
        }
        //Take action If storyId is not found
        bubble.setCreatedTime(AzazteUtils.time());
        MongoFactory.getMongoTemplate().save(bubble);
    }

    public void saveBubbleAdmin(Bubble bubble) {
        verifyChannelList(bubble);
        Query query = new Query();
        query.addCriteria(Criteria.where("storyId").is(bubble.getStoryId()));
        List<Bubble> bubbleAdmins = MongoFactory.getMongoTemplate().find(query, Bubble.class);
        if (bubbleAdmins.size() > 0) {
            Update update = new Update();
            update.set("channelDataList", bubble.getChannelDataList());
            update.set("answer", getchannelDataListString(bubble.getChannelDataList()));
            MongoFactory.getMongoTemplate().findAndModify(query, update, Bubble.class);
        } else {
            bubble.setAnswer(getchannelDataListString(bubble.getChannelDataList()));
            MongoFactory.getMongoTemplate().save(bubble);
        }
    }

    private String getchannelDataListString(List<ChannelData> channelDataList) {
        String channels = "";
        for (ChannelData channelData : channelDataList) {
            channels = channels + "," + channelData.getChannelName();
        }
        return channels;
    }

    private void verifyChannelList(Bubble bubbleAdmin) {
        List<ChannelData> channelDataList = bubbleAdmin.getChannelDataList();
        for (ChannelData channelData : channelDataList) {
            channelData.setChannelName(channelData.getChannelName().toUpperCase());
        }
    }

    public void removeBubble(String id) {
        //MongoFactory.getMongoTemplate().remo
    }

    public List<Bubble> fetchAllBubbles() {
        return MongoFactory.getMongoTemplate().findAll(Bubble.class);
    }
}
