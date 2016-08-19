package com.azazte.User;

import com.azazte.Beans.Credentials;
import com.azazte.Beans.NewsCard;
import com.azazte.Beans.User;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by home on 30/06/16.
 */
public class UserService {
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
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
        Criteria userNameCriteria = Criteria.where(USERNAME).is(credentials.getUsername());
        Criteria criteria = Criteria.where(PASSWORD).is(credentials.getPassword()).andOperator(userNameCriteria);

        List<User> users = MongoFactory.getMongoTemplate().find(new Query(criteria), User.class);

        if (users.size() > 1) {
            return "Multiple users for same credentials..!!";
        }

        if (users.size() == 0) {
            users = MongoFactory.getMongoTemplate().find(new Query(userNameCriteria), User.class);
            if (users.size() == 0) {
                return "invalid username";
            } else {
                return "invalid credentials";
            }
        }
        return users.get(0).getId();
    }
}
