package com.azazte.webservice;

import com.azazte.TheReadingQ.TheReadingQService;
import com.azazte.utils.AzazteUtils;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by home on 27/01/17.
 */
@Path("/trq")
public class TheReadingQRestAPI {
    @POST
    @Path("push")
    public Response addToQueue(@QueryParam("url") String url, @QueryParam("userId") String userId) {
        return Response.ok(AzazteUtils.toJson(TheReadingQService.getInstance().addToQueue(url, userId))).build();
    }

    @GET  //POST
    @Path("push")
    public Response addToQueueBrowserTest(@QueryParam("url") String url, @QueryParam("userId") String userId) {
        return Response.ok(AzazteUtils.toJson(TheReadingQService.getInstance().addToQueue(url, userId))).build();
    }

    @GET
    @Path("pop")
    public Response getFromQueue(@QueryParam("userId") String userId) {
        return Response.ok(AzazteUtils.toJson(TheReadingQService.getInstance().getFromQueue(userId))).build();
    }

    @DELETE
    @Path("/")
    public Response deleteFromQueue(@QueryParam("userId") String userId, @QueryParam("url") String url) {
        return Response.ok(AzazteUtils.toJson(TheReadingQService.getInstance().deleteFromQueue(url, userId))).build();
    }

    @GET // Delete
    @Path("/")
    public Response deleteFromQueueBrowserTest(@QueryParam("userId") String userId, @QueryParam("url") String url) {
        return Response.ok(AzazteUtils.toJson(TheReadingQService.getInstance().deleteFromQueue(url, userId))).build();
    }

}
