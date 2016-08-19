package com.azazte.webservice;

import com.azazte.Beans.NewsCard;
import com.azazte.News.NewsService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("admin")
public class AdminPanelRestAPI {

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNews(NewsCard newsCard) {
        newsCard.setApproved(false);
        NewsService.getInstance().saveNews(newsCard);
        return Response.ok(newsCard.getId()).build();
    }

    @GET
    @Path("approve")
    public Response approveNews(@QueryParam("newsId") String newsId, @QueryParam("approve") Boolean approve) {
        NewsService.getInstance().approveNews(newsId, approve);
        return Response.ok(newsId).build();
    }
}
