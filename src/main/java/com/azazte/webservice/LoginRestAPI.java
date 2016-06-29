package com.azazte.webservice;

import com.azazte.Beans.Credentials;
import com.azazte.Beans.User;
import com.azazte.User.UserService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by home on 30/06/16.
 */
@Path("login")
public class LoginRestAPI {
    @Path("register")
    @POST
    public Response register(User user) {
        UserService.getInstance().registerUser(user);
        return null;
    }

    @POST
    @Path("/")
    public Response login(Credentials credentials) {
        UserService.getInstance().validateUser(credentials);
        return null;
    }
}
