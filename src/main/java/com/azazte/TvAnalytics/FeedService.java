package com.azazte.TvAnalytics;

import com.azazte.TvAnalytics.CBIR.CBIRService;
import com.azazte.webservice.TvAnalyticsRestAPI;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Feed;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by dhruv.suri on 21/02/17.
 */
public class FeedService {
    private static FeedService instance = new FeedService();

    private String baseUrl = "http://dittotv.live-s.cdn.bitgravity.com/cdn-live/_definst_/dittotv/secure/sony_ent_Web.smil/";
    private Set<String> alreadyFetchedVideos = new HashSet<>();

    public static FeedService getInstance() {
        return instance;
    }

    public void fetchVideoFeeds(String chunkUrl) {
        try {
            String videoUrl = DefaultPaths.defaultFeedPath + new Date() + ".ts";
            FileOutputStream videoStream = new FileOutputStream(videoUrl);

            while (TvAnalyticsRestAPI.isPollingEnabled) {
                System.out.println("Polling");
                Thread.sleep(3000);
                List<String> videoListFromChunkList = getVideoListFromChunkList(chunkUrl);
                if (videoListFromChunkList == null || videoListFromChunkList.size() == 0) {
                    continue;
                }


                for (String url : videoListFromChunkList) {
                    int indexOf = url.indexOf(".ts");
                    alreadyFetchedVideos.add(url.substring(0, indexOf));
                    if (alreadyFetchedVideos.size() % 10 == 0) {
                        videoStream.close();
                        CentralQueue.videoQ.add(videoUrl);
                        videoUrl = DefaultPaths.defaultFeedPath + new Date() + ".ts";
                        videoStream = new FileOutputStream(videoUrl);
                    }
                    HttpGet getRequest = new HttpGet(baseUrl + url);
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpResponse response = httpClient.execute(getRequest);
                    System.out.println(url);
                    if (response.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Video GET call broken.Something seriously wrong\nNot processing further");
                        continue;
                    }

                    ArrayList<Byte> videoList = new ArrayList<Byte>();

                    long contentLength = ((BasicHttpResponse) response).getEntity().getContentLength();

                    int i = 0;


                    while (i < contentLength) {
                        byte read = (byte) ((BasicHttpResponse) response).getEntity().getContent().read();
                        i++;
                        videoList.add(read);
                    }

                    videoStream.write(ArrayUtils.toPrimitive(videoList.toArray(new Byte[videoList.size()])));

                    httpClient.getConnectionManager().shutdown();
                }

            }

        } catch (Exception ignored) {

        }
    }


    public void convertVideoFeedToImage() {

        try {
            while (true) {
                //Ad break condition
                if (CentralQueue.videoQ.size() == 0) {
                    //System.out.println("Waiting for video in video feed");
                    Thread.sleep(2000);
                    continue;
                }
                VideoToImageConvertor.getInstance().convert(CentralQueue.videoQ.remove(), DefaultPaths.defaultImagePath, DefaultPaths.frameRate, CentralQueue.imageQ);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void identifyAd() {
        try {
            while (true) {
                if (CentralQueue.imageQ.size() == 0) {
                    //System.out.println("Waiting for video in video feed");
                    Thread.sleep(2000);
                }
                String path = "file://" + Paths.get(CentralQueue.imageQ.remove()).toAbsolutePath().toString();
                String bucketName = CBIRService.getInstance().identifyBucket(path);
                if (bucketName == null) {
                    continue;
                }
                System.out.println(bucketName + " at time : " + new Date() + " and image url is " + path); // TODO :  add image name as well
            }
        } catch (Exception ignored) {

        }
    }

    private List<String> getVideoListFromChunkList(String url) {

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Chunklist get call broken" + response.getStatusLine().getReasonPhrase());
                return null;
            }
            return parseFileResponse(response.getEntity().getContent());
            //return parseFileResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> parseFileResponse(InputStream response) {

        List<String> videosToFetch = new ArrayList<>();

        BufferedReader br = new BufferedReader(
                new InputStreamReader(response));
        String url;
        try {
            while ((url = br.readLine()) != null) {
                if (url.startsWith("media_")) {
                    url = url + br.readLine();
                    int i = url.indexOf(".ts");
                    if (!alreadyFetchedVideos.contains(url.substring(0, i))) {
                        videosToFetch.add(url);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return videosToFetch;
    }


}
