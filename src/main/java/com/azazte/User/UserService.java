package com.azazte.User;

import com.azazte.Beans.Credentials;
import com.azazte.Beans.User;

/**
 * Created by home on 30/06/16.
 */
public class UserService {
    private static UserService userService = new UserService();

    private UserService() {

    }

    public static UserService getInstance() {
        return userService;
    }

    public String registerUser(User user) {
        //Do something
        return null;
    }

    public String validateUser(Credentials credentials) {
        //Do something
        return null;
    }
}
