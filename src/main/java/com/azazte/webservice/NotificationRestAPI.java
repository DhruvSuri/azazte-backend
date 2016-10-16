package com.azazte.webservice;

import com.azazte.Beans.NotificationConfig;
import com.azazte.Notification.NotificationService;
import com.azazte.utils.AzazteUtils;
import jersey.repackaged.com.google.common.base.Joiner;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;

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
    @Path("/sendNotification")
    public Response getNotificationCount() {
        List<NotificationConfig> notificationConfigs = NotificationService.getInstance().getAllNotificationObjects();
        PrintWriter pw = new PrintWriter(System.out);

        for (NotificationConfig notificationConfig : notificationConfigs) {
            pw.println(notificationConfig.getRegId());
        }
        pw.flush();
        return Response.ok(AzazteUtils.toJson(pw.toString())).build();
    }
}
