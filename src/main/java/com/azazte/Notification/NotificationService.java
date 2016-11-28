package com.azazte.Notification;

import com.azazte.Beans.NotificationConfig;
import com.azazte.Beans.NotificationMessageWrapper;
import com.azazte.Beans.NotificationObject;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by home on 15/08/16.
 */
public class NotificationService {
    private static NotificationService notificationService;
    private static String Authorization = "key=AIzaSyAFFoodkCMGDWwpG9bltfyq4LluO9N-n7o";
    private String test1 = "Lenovo_TOEALFRKY9NBS4OV_LRX21M";
    private String test2 = "motorola_TA93401LOA_LPB23.13-56";
    private String test3 = "motorola_ZY22326HRS_MPD24.107-56";

    public static NotificationService getInstance() {
        if (notificationService == null) {
            notificationService = new NotificationService();
        }
        return notificationService;
    }

    public void save(NotificationConfig requestDTO) {
        validate(requestDTO);
        requestDTO.setModifiedTime(AzazteUtils.time());
        MongoTemplate mongoTemplate = MongoFactory.getMongoTemplate();
        NotificationConfig config = findById(requestDTO.getDeviceId());
        if (config != null) {
            requestDTO.setId(config.getId());
            if (StringUtils.isEmpty(requestDTO.getRegId())) {
                requestDTO.setId(config.getId());
                requestDTO.setCreatedTime(config.getCreatedTime());
                requestDTO.setStatus(NotificationStatus.REG_FAILED);
            }
        } else {
            long time = AzazteUtils.time();
            requestDTO.setCreatedTime(time);
            requestDTO.setModifiedTime(time);
        }
        mongoTemplate.save(requestDTO);
    }

    private NotificationConfig findById(String deviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceId").is(deviceId));
        return MongoFactory.getMongoTemplate().findOne(query, NotificationConfig.class);
    }

    private void validate(NotificationConfig requestDTO) {
        if (requestDTO.getDeviceId() == null) {
            throw new RuntimeException("Device Id cannot be null");
        }
    }

    public List<NotificationConfig> getAllNotificationObjects() {
        return MongoFactory.getMongoTemplate().findAll(NotificationConfig.class);
    }

    private List<String> getAllNotificationIds() {
        List<String> notificationIds = Lists.newArrayList();
        for (NotificationConfig notificationConfig : getAllNotificationObjects()) {
            notificationIds.add(notificationConfig.getRegId());
        }
        return notificationIds;
    }

    private List<String> getTestNotificationIds() {
        List<String> notificationIds = new ArrayList<>();
        notificationIds.add(findById(test1).getRegId());
        notificationIds.add(findById(test2).getRegId());
        notificationIds.add(findById(test3).getRegId());
        return notificationIds;
    }

    public void sendNotification(NotificationMessageWrapper notificationMessage, boolean testFlag) {

        NotificationObject notificationObject = new NotificationObject();
        notificationObject.setData(notificationMessage);

        if (testFlag) {
            notificationObject.setRegistration_ids(getTestNotificationIds());
            executeNotification(notificationObject);
        } else {
            List<String> dummyList;
            List<String> allNotificationIds = getAllNotificationIds();
            System.out.println(allNotificationIds.size());
            for (String notificationId : allNotificationIds) {
                dummyList = Lists.newArrayList();
                dummyList.add(notificationId);
                notificationObject.setRegistration_ids(dummyList);
                executeNotification(notificationObject);
            }
        }

    }

    private void executeNotification(NotificationObject notificationObject) {
        try {
            StringEntity entity = new StringEntity(AzazteUtils.toJson(notificationObject));
            String url = "https://gcm-http.googleapis.com/gcm/send";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            // add header
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "key=AIzaSyAFFoodkCMGDWwpG9bltfyq4LluO9N-n7o");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
