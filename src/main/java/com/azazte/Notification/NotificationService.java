package com.azazte.Notification;

import com.azazte.Beans.NotificationConfig;
import com.azazte.mongo.MongoFactory;
import com.azazte.utils.AzazteUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by home on 15/08/16.
 */
public class NotificationService {
    private static NotificationService notificationService;

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
        }else{
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
}
