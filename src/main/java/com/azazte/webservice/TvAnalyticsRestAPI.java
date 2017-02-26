package com.azazte.webservice;

import com.azazte.TvAnalytics.*;
import com.azazte.TvAnalytics.CBIR.CBIRService;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Feed;
import org.springframework.core.task.AsyncTaskExecutor;

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
        return Response.ok("Size of map is : " + CBIRService.map.size() + "\n values of map are : " + CBIRService.map).build();
    }

    @GET
    @Path("refreshImageQ")
    public Response refreshImageQ() {
        CentralQueue.imageQ.clear();
        return Response.ok().build();
    }

    @GET
    @Path("refreshVideoQ")
    public Response refreshVideoQ() {
        CentralQueue.videoQ.clear();
        return Response.ok().build();
    }

    @GET
    @Path("printVideoQueue")
    public Response printVideoQueue() {
        return Response.ok("Size of video Queue is :" + CentralQueue.videoQ.size() + " \n and Values of video Queue is : " + CentralQueue.videoQ).build();
    }

    @GET
    @Path("printImageQueue")
    public Response printImageQueue() {
        return Response.ok("Size of image Queue is :" + CentralQueue.imageQ.size() + " \n and Values of image Queue is : " + CentralQueue.imageQ).build();
    }

    @GET
    @Path("testPath")
    public void testPath() {
        try {
            new FileOutputStream("testFile.txt").close();
        } catch (Exception e) {

        }
    }


    /*
    MAin methods
     */

    //Index new ads
//    public static void main(String args[]){
//        VideoToImageConvertor.getInstance().convert("/Users/dhruv.suri/Documents/project/azazte-backend/Videos/Ad Videos/Cadbury_Silk_Oreo.mp4","/Users/dhruv.suri/Documents/project/azazte-backend/Videos/Ads/Cadbury_Silk_Oreo/", DefaultPaths.frameRate,null);
//    }


//    //Merge ad indexes
//    public static void main(String args[]){
//        AdService.getInstance().preprocessAdsForIndexing("/Users/dhruv.suri/Documents/project/azazte-backend/Videos/");
//    }

    //Analyze ads
    public static void main(String args[]){
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/11.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/12.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/13.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/14.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/15.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/16.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/17.ts");
        CentralQueue.videoQ.add("/Users/dhruv.suri/Documents/jetty/jetty-distribution-9.3.10.v20160621/base/feed/18.ts");
        FeedService.getInstance().convertVideoFeedToImage();
    }
}
