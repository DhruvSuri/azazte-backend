package com.azazte.webservice;

import com.azazte.Beans.NewsCard;
import com.azazte.Beans.NewsCardWrapper;
import com.azazte.News.NewsService;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by home on 27/06/16.
 */
@Path("restricted")
public class Restricted {


    @GET
    @Path("testGET")
    public Response test() {
        return Response.ok("Yes the server is up").build();
    }

    @POST
    @Path("testPOST")
    public Response testPost() {
        return Response.ok("Yes the server is up").build();
    }


    // Import the news from cpanel.
    @GET
    @Path("import")
    public Response importNews() {
        String url = "http://azazte.com/news/fetchNewsOnRefreshv3/0";
        StringBuilder response = new StringBuilder();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        NewsCardWrapper newsCardWrapper = AzazteUtils.fromJson(response.toString(), NewsCardWrapper.class);
        List<NewsCard> newsCardList = newsCardWrapper.getNewsCardList();
        for (NewsCard newsCard : newsCardList) {
            newsCard.setId(null);
            newsCard.setApproved(true);
            if (exists(newsCard)) {
                continue;
            }
            NewsService.getInstance().saveNews(newsCard);
        }

        return Response.ok("Import successful").build();
    }

    private boolean exists(NewsCard newsCard) {
        Criteria criteria = Criteria.where("newsHead").is(newsCard.getNewsHead());
        List<NewsCard> newsCards = MongoFactory.getMongoTemplate().find(new Query(criteria), NewsCard.class);
        return newsCards.size() != 0;
    }
}
