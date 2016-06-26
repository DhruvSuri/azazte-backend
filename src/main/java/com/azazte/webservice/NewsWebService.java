package com.azazte.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/message")
public class NewsWebService {
    @GET
    public String getMsg() {
        return "Hello World !! - Jersey 2";
    }

    @POST
    public String setMsg(@QueryParam("msg") String msg) {
        return msg;
    }
}