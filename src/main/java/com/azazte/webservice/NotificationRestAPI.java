package com.azazte.webservice;

import com.azazte.Beans.NotificationConfig;
import com.azazte.Notification.NotificationService;
import com.azazte.utils.AzazteUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        List<NotificationConfig> notificationObjects = NotificationService.getInstance().getAllNotificationObjects();
        return Response.ok(AzazteUtils.toJson(notificationObjects)).build();
    }
}
