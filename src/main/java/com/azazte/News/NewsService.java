package com.azazte.News;

import com.azazte.Beans.NewsCard;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import com.azazte.utils.Mail.MailConfig;
import com.azazte.utils.Mail.MailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (newsCard.getId().isEmpty()) {
            newsCard.setId(null);
        }
        if (newsCard.getImpactLabel() == null || newsCard.getImpactLabel().isEmpty()) {
            newsCard.setImpactLabel("IMPACT");
        }
        formatDate(newsCard);
        MongoFactory.getMongoTemplate().save(newsCard);
        sendMail(newsCard);
    }

    private void sendMail(NewsCard newsCard) {
        try {
            MailConfig mailConfig = MongoFactory.getMongoTemplate().findOne(new Query(), MailConfig.class);
            if (mailConfig == null) {
                return;
            }
            MailUtils.sendMail(newsCard.getNewsHead(), newsCard.getNewsBody(), mailConfig.getMailList());
        } catch (Exception ignored) {
        }
    }

    private void formatDate(NewsCard newsCard) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = simpleDateFormat.parse(newsCard.getDate());
            simpleDateFormat.applyPattern("MMM dd, yyyy");
            newsCard.setDate(simpleDateFormat.format(parse));
            newsCard.setCreatedTime(parse.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        if (isApproved != null) {
            query.addCriteria(Criteria.where(IS_APPROVED).is(isApproved));
        }
        List<NewsCard> newsCards = MongoFactory.getMongoTemplate().find(query, NewsCard.class);
        formatExistingDate(newsCards);
        return newsCards;
    }


    private void formatExistingDate(List<NewsCard> newsCards) {
        for (NewsCard newsCard : newsCards) {
            String dateString = newsCard.getDate();
            if (dateString.contains("-")) {
                formatDate(newsCard);
            }
        }
    }

    public List<NewsCard> fetchAllNews(Integer skip, Integer limit, Boolean category, Boolean multipleFlag) {
        List<NewsCard> newsCards = fetchNews(skip, limit, category);
        // Supporting the old app for multiple categories
        for (NewsCard newsCard : newsCards) {
            String categories = newsCard.getCategory();
            categories = categories.replaceAll(" ", "");
            if (!multipleFlag) {
                String[] split = categories.split(",");
                newsCard.setCategory(split[0]);
            } else {
                newsCard.setCategory(categories);
            }
        }

        return newsCards;
    }

    public void approveNews(String newsId, Boolean approve) {
        NewsCard news = findNewsById(newsId);
        if (news == null) {
            return;
        }
        if (news.getCreatedTimeEpoch() == null){
            news.setCreatedTimeEpoch(AzazteUtils.time());
        }
        news.setApproved(approve);
        saveNews(news);
    }

    public NewsCard findNewsById(String newsId) {
        if (newsId == null) {
            return null;
        }
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
