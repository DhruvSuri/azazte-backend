package com.azazte.webservice;

import com.azazte.Government.GovService;
import com.google.gson.Gson;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by home on 21/12/16.
 */
@Path("/gov")
public class GovernmentRestAPI {


    @Path("resources")
    @GET
    public Response getResources(@QueryParam("command") String commandName, @QueryParam("zone") String zoneName) {
        if (commandName == null) {
            return Response.ok(new Gson().toJson(GovService.getInstance().fetchCommandLevelResponse())).build();
        }
        if (zoneName == null) {
            return Response.ok(new Gson().toJson(GovService.getInstance().fetchZoneLevelResponse(commandName))).build();
        }
        return Response.ok(new Gson().toJson(GovService.getInstance().fetchLowestLevelRsponse(commandName, zoneName))).build();
    }

    @Path("preprocess")
    @GET
    public Response preProcess(@QueryParam("path") String path) {
        GovService.getInstance().preProcess(path);
        return Response.ok().build();
    }
}
