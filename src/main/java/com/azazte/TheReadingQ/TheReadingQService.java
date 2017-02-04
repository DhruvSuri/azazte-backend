package com.azazte.TheReadingQ;

import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import com.azazte.utils.Mail.MailUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by home on 27/01/17.
 */
public class TheReadingQService {
    private static TheReadingQService instance = null;

    private TheReadingQService() {
    }

    public static TheReadingQService getInstance() {
        if (instance == null) {
            instance = new TheReadingQService();
        }
        return instance;
    }

    public QResponse addToQueue(String url, String userId) {
        QResponse response = new QResponse();
        Query query = new Query();
        query.addCriteria(Criteria.where("url").is(url));
        query.addCriteria(Criteria.where("userId").is(userId));
        List<QItem> qItems = MongoFactory.getMongoTemplate().find(query, QItem.class);
        if (qItems.size() > 0) {
            response.setUrl(null);
            response.setNumberOfItemsRemaining(this.getItemCount(userId));
            response.setResponseText("URL already exists");
            return response;
        }

        QItem item = new QItem();
        item.setCreatedTime(AzazteUtils.time());
        item.setIsRead(0);
        item.setLastRead(0);
        item.setUrl(url);
        item.setUserId(userId);
        item.setIsDeleted(0);
        MongoFactory.getMongoTemplate().save(item);
        response.setNumberOfItemsRemaining(this.getItemCount(userId));
        response.setResponseText("Added Successfully");
        return response;
    }

    public QResponse getFromQueue(String userId) {
        QResponse response = new QResponse();
        List<QItem> activeItems = getActiveItems(userId);
        if (activeItems.size() == 0) {
            response.setUrl(null);
            response.setNumberOfItemsRemaining(0);
            response.setResponseText("No URLs available");
            return response;
        }

        int i = ThreadLocalRandom.current().nextInt(0, activeItems.size());

        QItem item = activeItems.get(i);
        item.setLastRead(AzazteUtils.time());
        MongoFactory.getMongoTemplate().save(item);
        response.setNumberOfItemsRemaining(activeItems.size());
        response.setUrl(item.getUrl());
        return response;
    }

    public QResponse deleteFromQueue(String url, String userId) {
        Query query = new Query();
        QResponse response = new QResponse();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("url").is(url));
        List<QItem> qItems = MongoFactory.getMongoTemplate().find(query, QItem.class);
        if (qItems.size() == 0) {
            response.setNumberOfItemsRemaining(getItemCount(userId));
            response.setResponseText("URL not found");
            return response;
        }

        QItem item = qItems.get(0);
        item.setIsDeleted(1);
        MongoFactory.getMongoTemplate().save(item);
        response.setNumberOfItemsRemaining(getItemCount(userId));
        response.setResponseText("Deleted successfully.. You wont see this link again");
        return response;
    }

    private int getItemCount(String userId) {
        return getActiveItems(userId).size();
    }

    private List<QItem> getActiveItems(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("isDeleted").is(0));
        return MongoFactory.getMongoTemplate().find(query, QItem.class);
    }
}
