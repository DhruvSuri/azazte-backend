package com.azazte.webservice;

import com.azazte.Beans.NewsCard;
import com.azazte.News.NewsService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    @GET
    @Path("image")
    public Response importImageToServer(@QueryParam("originalUrl") String originalImageUrl, @QueryParam("imageUrl") String url) {
        try (InputStream in = new URL(originalImageUrl).openStream()) {
            Files.copy(in, Paths.get("/home/ec2-user/jetty/jetty-distribution-9.3.10.v20160621/webapps/images/" + url));
        } catch (Exception e) {
            return Response.ok("Failed to copy image" + e).build();
        }
        return Response.ok("aws.azazte.com/images/" + url).build();
    }
}
