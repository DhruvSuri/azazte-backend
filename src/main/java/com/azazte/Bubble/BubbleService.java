package com.azazte.Bubble;

import com.azazte.Beans.Bubble;
import com.azazte.Beans.NewsCard;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
}
