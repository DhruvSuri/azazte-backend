package com.azazte.Beans;

import java.util.List;

/**
 * Created by home on 04/11/16.
 */
public class NotificationObject {
    private List<String> registration_ids;
    private NotificationMessageWrapper data;

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public NotificationMessageWrapper getData() {
        return data;
    }

    public void setData(NotificationMessageWrapper data) {
        this.data = data;
    }
}
