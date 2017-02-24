package com.azazte.TvAnalytics.CBIR;

import com.azazte.utils.AzazteUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dhruv.suri on 18/02/17.
 */
public class CBIRAPIExecutor {

    private String baseUrl = "http://localhost:9999/";
    private static CBIRAPIExecutor instance = new CBIRAPIExecutor();

    public static CBIRAPIExecutor getInstance() {
        return instance;
    }


    public List<SimilarityResponse> searchImage(String imageUrl) {


        String path = "/api/searchUrl";
        HttpPost postRequest = new HttpPost(baseUrl + path);
        postRequest.setHeader("authorization", "Basic YWRtaW46YWRtaW4=");
        postRequest.setHeader("cache-control", "no-cache");
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("url", imageUrl));


        try {
            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpResponse response = httpClient.execute(postRequest);
            String responseString = EntityUtils.toString(response.getEntity());
            return AzazteUtils.fromJson(responseString, CBIRResponseWrapper.class).getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
