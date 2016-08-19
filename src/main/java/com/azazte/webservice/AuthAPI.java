package com.azazte.webservice;

import com.azazte.Beans.Credentials;
import com.azazte.Beans.User;
import com.azazte.User.UserService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by home on 17/07/16.
 */

@Path("auth")
public class AuthAPI {
    @Path("register")
    @POST
    public Response register(User user) {
        UserService.getInstance().registerUser(user);
        return null;
    }

    @POST
    @Path("/")
    public Response login(Credentials credentials) {
        credentials.validate();
        String response = UserService.getInstance().validateUser(credentials);
        return Response.ok(response).build();
    }
}