package com.azazte.Beans;

/**
 * Created by home on 04/11/16.
 */
public class NotificationMessageWrapper {
    private NotificationMessage message;

    public NotificationMessageWrapper() {
        this.message = new NotificationMessage();
    }


    public NotificationMessage getMessage() {
        return message;
    }

    public void setMessage(NotificationMessage message) {
        this.message = message;
    }

    public void setHeadline(String headline) {
        this.message.setHeadline(headline);
    }

    public void setImageUrl(String imageUrl) {
        this.message.setImageUrl(imageUrl);
    }

    public void setId(String id) {
        this.message.setId(id);
    }

    public static class NotificationMessage {
        private String imageUrl;
        private String headline;
        private String id;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
