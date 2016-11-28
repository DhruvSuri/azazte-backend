package com.azazte.webservice;

import com.azazte.Beans.*;
import com.azazte.Bubble.BubbleService;
import com.azazte.News.NewsService;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import jersey.repackaged.com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.BufferUnderflowException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        List<Bubble> bubbles;
        if (id == null) {
            bubbles = BubbleService.getInstance().fetchAllBubbles();
        } else {
            bubbles = BubbleService.getInstance().fetchBubbles(id, start, limit);
        }
        return Response.ok(AzazteUtils.toJson(bubbles)).build();
    }

    @GET
    @Path("saveTemp")
    public Response saveBubbleTemp(@QueryParam("id") String id, @QueryParam("question") String question, @QueryParam("answer") String answer) {
        Bubble bubble = new Bubble();
        bubble.setStoryId(id);
        bubble.setAnswer(answer);
        saveBubble(bubble);
        return Response.ok("Bubble saved successfully").build();
    }

    @GET
    @Path("removeTemp")
    public Response removeBubbleTemp(@QueryParam("id") String id) {
        BubbleService.getInstance().removeBubble(id);
        return Response.ok("Bubble Removed").build();
    }

    @POST
    @Path("save")
    public Response saveBubble(Bubble bubble) {
        BubbleService.getInstance().saveBubble(bubble);
        return Response.ok().build();
    }

    @POST
    @Path("save/{storyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveBubble(@PathParam("storyId") String storyId, List<ChannelData> channelDataList) {
        if (channelDataList.size() == 0) {
            return Response.ok("Channel List empty").build();
        }
        Bubble bubble = new Bubble();
        bubble.setStoryId(storyId);
        bubble.setChannelDataList(channelDataList);
        BubbleService.getInstance().saveBubbleAdmin(bubble);
        return Response.ok().build();
    }

    @GET
    @Path("/{storyId}")
    public Response fetchBubbles(@PathParam("storyId") String storyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("storyId").is(storyId));
        List<Bubble> bubbleList = MongoFactory.getMongoTemplate().find(query, Bubble.class);
        if (bubbleList.size() > 0) {
            List<ChannelData> channelDataList = bubbleList.get(0).getChannelDataList();
            return Response.ok(AzazteUtils.toJson(channelDataList)).build();
        }
        return Response.ok().build();
    }

    @GET
    @Path("redirect")
    public Response linkRedirect(@QueryParam("storyId") String storyId, @QueryParam("channelName") String channelName) {
        Query query = new Query();

        query.addCriteria(Criteria.where("storyId").is(storyId));
        Bubble bubbleAdmin = MongoFactory.getMongoTemplate().findOne(query, Bubble.class);
        if (bubbleAdmin != null) {
            for (ChannelData channelData : bubbleAdmin.getChannelDataList()) {
                if (channelData.getChannelName().equals(channelName)) {
                    try {
                        URI location = new URI(channelData.getLink());
                        return Response.seeOther(location).build();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return Response.seeOther(URI.create("http://www.finup.in")).build();
    }


}
