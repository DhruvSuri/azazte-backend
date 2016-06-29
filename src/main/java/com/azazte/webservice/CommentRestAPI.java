package com.azazte.webservice;

import com.azazte.Beans.Comment;
import com.azazte.News.NewsService;
import com.azazte.mongo.MongoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("comment")
public class CommentRestAPI {
    Logger logger = LoggerFactory.getLogger(CommentRestAPI.class);

    @POST
    @Path("save/{newsId}")
    @Produces("application/json")
    public void saveComment(@PathParam("newsId") String newsId, Comment comment) {
        logger.debug("Saving comment");
        MongoFactory.getMongoTemplate().save(comment);
    }

    @GET
    @Path("/")
    @Produces("application/json")
    public Response getComment(@QueryParam("newsId") String newsId) {
        logger.debug("Fetching comment");
        Criteria criteria = Criteria.where("newsId").is(newsId);

        List<Comment> comments = MongoFactory.getMongoTemplate().find(new Query(criteria), Comment.class);
        return Response.ok("test").build();
    }

    @GET
    @Path("like")
    public String getLikes(@QueryParam("newsId") String newsId) {
        return NewsService.getInstance().fetchLikes(newsId).toString();
    }

    @POST
    @Path("like")
    public void doLike(@QueryParam("newsId") String newsId) {
        NewsService.getInstance().likeNews(newsId);
    }
}
