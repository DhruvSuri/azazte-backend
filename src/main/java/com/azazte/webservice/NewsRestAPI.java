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
    public Response getAllApprovedNews(@QueryParam("start") Integer start, @QueryParam("limit") Integer limit) {
        List<NewsCard> allNews = NewsService.getInstance().fetchAllApprovedNews(start, limit);
        NewsCardWrapper newsCardWrapper = new NewsCardWrapper();
        newsCardWrapper.setNewsCardList(allNews);
        return Response.ok(AzazteUtils.toJson(newsCardWrapper)).build();
    }

    @GET
    @Path("/approved")
    public Response getAllNews(@QueryParam("start") Integer start, @QueryParam("limit") Integer limit) {
        List<NewsCard> allNews = NewsService.getInstance().fetchAllNews(start, limit);
        NewsCardWrapper newsCardWrapper = new NewsCardWrapper();
        newsCardWrapper.setNewsCardList(allNews);
        return Response.ok(AzazteUtils.toJson(newsCardWrapper)).build();
    }
}