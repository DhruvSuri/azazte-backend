package com.azazte.News;

import com.azazte.Beans.NewsCard;
import com.azazte.mongo.MongoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by home on 27/06/16.
 */
public class NewsService {
    private static NewsService newsService = new NewsService();
    public static final String IS_APPROVED = "isApproved";
    public static final String ID = "id";
    Logger logger = LoggerFactory.getLogger(NewsService.class);

    private NewsService() {
    }

    public static NewsService getInstance() {
        return newsService;
    }

    public void saveNews(NewsCard newsCard) {
        if (newsCard.getLikes() == null) {
            newsCard.setLikes(0);
        }
        MongoFactory.getMongoTemplate().save(newsCard);
    }

    private List<NewsCard> fetchNews(Integer skip, Integer limit, Boolean isApproved) {
        Query query = new Query();
        if (limit == null) {
            limit = 10;
        }
        if (skip == null) {
            skip = 0;
        }
        query.limit(limit);
        query.skip(skip);
        query.with(new Sort(Sort.Direction.DESC, ID));
        Criteria criteria = Criteria.where(IS_APPROVED).is(isApproved);
        return MongoFactory.getMongoTemplate().find(query, NewsCard.class);
    }

    public List<NewsCard> fetchAllApprovedNews(Integer skip, Integer limit) {
        return fetchNews(skip, limit, true);
    }

    public List<NewsCard> fetchAllNews(Integer skip, Integer limit) {
        return fetchNews(skip, limit, false);
    }

    public void approveNews(String newsId) {
        NewsCard news = findNewsById(newsId);
        news.setApproved(true);
        saveNews(news);
    }

    private NewsCard findNewsById(String newsId) {
        Criteria criteria = Criteria.where(ID).is(newsId);
        List<NewsCard> newsCards = MongoFactory.getMongoTemplate().find(new Query(criteria), NewsCard.class);

        if (newsCards.size() > 1) {
            logger.error("Something is terribly wrong with news.More than one news for same Id");
        }
        return newsCards.get(0);
    }

    public void likeNews(String newsId) {
        NewsCard newsById = findNewsById(newsId);
        if (newsById == null) {
            return;
        }
        Integer likes = newsById.getLikes();
        if (likes == null) {
            likes = 1;
        } else {
            likes++;
        }
        newsById.setLikes(likes);
        saveNews(newsById);
    }

    public Integer fetchLikes(String newsId) {
        NewsCard newsCard = findNewsById(newsId);
        if (newsCard == null) {
            logger.error("Fetching likes for a non existing news " + newsId);
            return 0;
        }
        return newsCard.getLikes();
    }
}
