package com.azazte.webservice;

import com.azazte.Beans.NewsCard;
import com.azazte.Beans.NotificationConfig;
import com.azazte.Beans.NotificationMessageWrapper;
import com.azazte.News.NewsService;
import com.azazte.Notification.NotificationService;
import com.azazte.utils.AzazteUtils;
import com.azazte.utils.TokenService;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 15/08/16.
 */
@Path("/notification")
public class NotificationRestAPI {
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveNotificationDetails(NotificationConfig requestDTO) {
        NotificationService.getInstance().save(requestDTO);
        return null;
    }

    @GET
    @Path("/")
    public Response getNotificationData() {
        List<NotificationConfig> notificationConfigs = NotificationService.getInstance().getAllNotificationObjects();
        List<String> notificationIds = Lists.newArrayList();
        for (NotificationConfig notificationConfig : notificationConfigs) {
            notificationIds.add(notificationConfig.getRegId());
        }
        return Response.ok(AzazteUtils.toJson(notificationIds)).build();
    }

    @GET
    @Path("/count")
    public Response getNotification() {
        List<NotificationConfig> notificationConfigs = NotificationService.getInstance().getAllNotificationObjects();
        return Response.ok(AzazteUtils.toJson(notificationConfigs.size())).build();
    }

    @GET
    @Path("/send")
    public Response getNotificationCount(@QueryParam("storyId") String storyId, @QueryParam("sendImage") boolean imageFlag, @QueryParam("customHeadline") String customHeadline, @QueryParam("customImage") String customImage, @QueryParam("test") boolean testFlag, @QueryParam("token") String token) {
        if (!TokenService.getInstance().validateToken(token)) {
            return Response.ok("Invalid Token").build();
        }
        NewsCard news = NewsService.getInstance().findNewsById(storyId);
        NotificationMessageWrapper wrapper = new NotificationMessageWrapper();

        if (customHeadline != null) {
            wrapper.setHeadline(customHeadline);
        } else {
            wrapper.setHeadline(news.getNewsHead());
        }

        if (imageFlag) {
            if (customImage != null) {
                wrapper.setImageUrl(customImage);
            } else {
                wrapper.setImageUrl(news.getImageUrl());
            }
        }

        wrapper.setId(storyId);
        NotificationService.getInstance().sendNotification(wrapper, testFlag);
        return Response.ok("Sent successfully").build();
    }
}
