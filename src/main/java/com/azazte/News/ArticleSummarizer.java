package com.azazte.News;

import com.azazte.Beans.Summary;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by home on 27/10/16.
 */
public class ArticleSummarizer {
    private static String BASE_DOMAIN = "http://www.awesummly.com/demo/?encode=en&url=";
    private static ArticleSummarizer summarizer = new ArticleSummarizer();

    public static ArticleSummarizer getInstance() {
        return summarizer;
    }

//    public static void main(String args[]) {
//        String url = "http://economictimes.indiatimes.com/news/economy/indicators/not-discouraged-by-one-spot-jump-in-ease-of-biz-rankings-government/articleshow/55080820.cms";
//        System.out.println(AzazteUtils.toJson(summarizer.getSummary(url)));
//    }

    public Summary getSummary(String url) {
        String rawResponse = getRawResponse(url);
        String title = extractTags(rawResponse, "Title");
        String image = extractTags(rawResponse, "Image");
        String summary = extractTags(rawResponse, "Summary");
        return new Summary(title, summary, image);
    }

    private String extractTags(String rawResponse, String head) {
        String baseString = head + "</code></td><td>";
        int start = rawResponse.indexOf(baseString) + baseString.length();
        int end = rawResponse.indexOf("</td>", start);
        String response = rawResponse.substring(start, end);
        if (head.equals("Image")) {
            baseString = "href=";
            start = response.indexOf(baseString) + baseString.length();
            end = response.indexOf(">", start);
            response = response.substring(start, end);
            response = response.replaceAll("\"", "");
        }
        return response;
    }

    private String getRawResponse(String url) {
        String output = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(BASE_DOMAIN + url);
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


            String buff;
            System.out.println("Output from Server .... \n");
            while ((buff = br.readLine()) != null) {
                output = output + buff;
            }

            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {

        }
        return output;
    }
}
