package com.azazte.webservice;

import com.azazte.News.NewsService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("admin")
public class AdminPanelRestAPI {

    @POST
    @Path("save")
    public Response saveNews(String newsCard) {
        System.out.println("news");
        return Response.ok("test").build();
    }

    @GET
    @Path("adminSaveTest")
    public Response testAdmin(String newsCard) {
        System.out.println("news");
        return Response.ok("test").build();
    }

    @GET
    @Path("approve")
    public Response approveNews(@QueryParam("newsId") String newsId) {
        NewsService.getInstance().approveNews(newsId);
        return Response.ok(newsId).build();
    }
}
