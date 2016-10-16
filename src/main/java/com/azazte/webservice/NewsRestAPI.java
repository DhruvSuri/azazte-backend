package com.azazte.webservice;

import com.azazte.Beans.NewsCard;
import com.azazte.Beans.NewsCardWrapper;
import com.azazte.News.NewsService;
import com.azazte.utils.AzazteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/news")
public class NewsRestAPI {
    Logger logger = LoggerFactory.getLogger(NewsRestAPI.class);

    @GET
    public Response getNews(@QueryParam("start") Integer start, @QueryParam("limit") Integer limit, @QueryParam("filter") String filter, @QueryParam("multipleFlag") Boolean multipleFlag) {
        List<NewsCard> allNews = null;
        if (filter == null) {
            filter = "approved";
        }
        if (multipleFlag == null) {
            multipleFlag = false;
        }
        switch (filter) {
            case "all":
                allNews = NewsService.getInstance().fetchAllNews(start, limit, null, multipleFlag);
                break;
            case "pending":
                allNews = NewsService.getInstance().fetchAllNews(start, limit, false, multipleFlag);
                break;
            case "approved":
                allNews = NewsService.getInstance().fetchAllNews(start, limit, true, multipleFlag);
                break;
        }

        NewsCardWrapper newsCardWrapper = new NewsCardWrapper();
        newsCardWrapper.setNewsCardList(allNews);
        return Response.ok(AzazteUtils.toJson(newsCardWrapper)).build();
    }

    /*
    * Supporting old endpoint for app
    * */

    @GET
    @Path("fetchNewsOnRefresh3/{count}")
    public Response getNewsForApp(@PathParam("count") Integer count) {
        return getNews(0, count, "approved", false);
    }
}