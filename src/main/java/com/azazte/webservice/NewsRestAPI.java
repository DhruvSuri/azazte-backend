package com.azazte.webservice;

import com.azazte.Beans.NewsCard;
import com.azazte.Beans.NewsCardWrapper;
import com.azazte.News.NewsService;
import com.azazte.utils.AzazteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/news")
public class NewsRestAPI {
    Logger logger = LoggerFactory.getLogger(NewsRestAPI.class);

    @GET
    public Response getAllApprovedNews(@QueryParam("start") Integer start, @QueryParam("limit") Integer limit, @QueryParam("filter") String filter) {
        List<NewsCard> allNews;
        if (filter == null) {
            filter = "default";
        }
        switch (filter) {
            case "all":
                allNews = NewsService.getInstance().fetchAllNews(start, limit);
                break;
            case "pending":
                allNews = NewsService.getInstance().fetchPendingNews(start, limit);
                break;
            default:
                allNews = NewsService.getInstance().fetchAllApprovedNews(start, limit);
                break;
        }

        NewsCardWrapper newsCardWrapper = new NewsCardWrapper();
        newsCardWrapper.setNewsCardList(allNews);
        return Response.ok(AzazteUtils.toJson(newsCardWrapper)).build();
    }
}