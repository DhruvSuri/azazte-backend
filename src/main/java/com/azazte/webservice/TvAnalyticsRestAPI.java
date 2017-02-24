package com.azazte.webservice;

import com.azazte.TvAnalytics.CBIR.CBIRService;
import com.azazte.TvAnalytics.CentralQueue;
import com.azazte.TvAnalytics.FeedService;
import com.azazte.TvAnalytics.AdService;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Feed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by dhruv.suri on 07/02/17.
 */
@Path("/tv")
public class TvAnalyticsRestAPI {
    public static Boolean isPollingEnabled = true;

    @GET
    @Path("fetchFeed")
    public Response startPollingFeed(@QueryParam("chunkUrl") String chunkUrl) {
        isPollingEnabled = true;
        FeedService.getInstance().fetchVideoFeeds(chunkUrl);
        return Response.ok().build();
    }

    @GET
    @Path("convertVideoToImage")
    public Response convertVtoI() {
        FeedService.getInstance().convertVideoFeedToImage();
        return Response.ok().build();
    }

    @GET
    @Path("analyze")
    public Response startAnalyzing() {
        FeedService.getInstance().identifyAd();
        return Response.ok().build();
    }

    @GET
    @Path("stopPolling")
    public Response stopPolling() {
        isPollingEnabled = false;
        return Response.ok().build();
    }

    @GET
    @Path("preprocess")
    public Response preprocess(@QueryParam("adFolderPath") String adFolderPath) {
        AdService.getInstance().preprocessAdsForIndexing(adFolderPath);
        return Response.ok().build();
    }

    @GET
    @Path("refreshMap")
    public Response refreshAdMap() {
        CBIRService.getInstance().refreshMap();
        return Response.ok().build();
    }

    @GET
    @Path("printVideoQueue")
    public Response printVideoQueue() {
        return Response.ok("Size of video Queue is : + CentralQueue.videoQ.size()" + "  and Values of video Queue is : " + CentralQueue.videoQ).build();
    }

    @GET
    @Path("printImageQueue")
    public Response printImageQueue() {
        return Response.ok("Size of video Queue is :" + CentralQueue.imageQ.size() + "  and Values of image Queue is : " + CentralQueue.imageQ).build();
    }

    @GET
    @Path("testPath")
    public void testPath() {
        try {
            new FileOutputStream("testFile.txt").close();
        } catch (Exception e) {

        }
    }

}
