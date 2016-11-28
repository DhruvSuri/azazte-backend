package com.azazte.webservice;

import com.azazte.News.ArticleSummarizer;
import com.azazte.Beans.Summary;
import com.azazte.utils.AzazteUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


/**
 * Created by home on 27/10/16.
 */
@Path("/summary")
public class SummaryRestAPI {
    @GET
    @Path("/")
    public Response getSummary(@QueryParam("url") String url) {
        Summary summary = ArticleSummarizer.getInstance().getSummary(url);
        return Response.ok(AzazteUtils.toJson(summary)).build();
    }


//    public static void main(String args[]) {
//        returnObjects();
//    }
//
//    private static void returnObjects() {
//        String json = "";
//        Type listType = new TypeToken<List<mixpanelObject>>() {
//        }.getType();
//        List<mixpanelObject> yourList = new Gson().fromJson(json, listType);
//        System.out.println(new Gson().toJson(yourList));
//    }
//
//    class mixpanelObject {
//        private String $distinct_id;
//
//        public String get$distinct_id() {
//            return $distinct_id;
//        }
//
//        public void set$distinct_id(String $distinct_id) {
//            this.$distinct_id = $distinct_id;
//        }
//    }

}
