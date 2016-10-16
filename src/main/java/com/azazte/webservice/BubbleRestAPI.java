package com.azazte.webservice;

import com.azazte.Beans.Bubble;
import com.azazte.Beans.NewsCard;
import com.azazte.Beans.NewsCardWrapper;
import com.azazte.Bubble.BubbleService;
import com.azazte.News.NewsService;
import com.azazte.utils.AzazteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.nio.BufferUnderflowException;
import java.util.List;

/**
 * Created by home on 13/08/16.
 */
@Path("/bubble")
public class BubbleRestAPI {
    Logger logger = LoggerFactory.getLogger(BubbleRestAPI.class);
    private Integer start = 0;
    private Integer limit = 10;

    @GET
    @Path("/")
    public Response getBubbles(@QueryParam("storyId") String id) {
        //Control pagination from backend.Default values 0,10
        //Need to make a pagination config where we control the pagination of various entities
        List<Bubble> bubbles = BubbleService.getInstance().fetchBubbles(id, start, limit);
        return Response.ok(AzazteUtils.toJson(bubbles)).build();
    }

    @GET
    @Path("saveTemp")
    public Response saveBubbleTemp(@QueryParam("id") String id, @QueryParam("question") String question, @QueryParam("answer") String answer) {
        saveBubble(new Bubble(id,question,answer));
        return Response.ok("Bubble saved successfully").build();
    }

    @GET
    @Path("removeTemp")
    public Response removeBubbleTemp(@QueryParam("id") String id){
        BubbleService.getInstance().removeBubble(id);
        return Response.ok("Bubble Removed").build();
    }

    @POST
    @Path("save")
    public Response saveBubble(Bubble bubble) {
        BubbleService.getInstance().saveBubble(bubble);
        return Response.ok().build();
    }
}
