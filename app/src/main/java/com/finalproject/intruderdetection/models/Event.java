package com.finalproject.intruderdetection.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by TKPC on 7/2/2018.
 */

public class Event {
    @SerializedName("event_id")
    public String eventId;
    @SerializedName("owner")
    private String userId;
    private String created;
    private String status;
    private String avatar;
    private String username;



    public Event(String eventId, String userId, String created, String status) {
        this.eventId = eventId;
        this.userId = userId;
        this.created = created;
        this.status = status;
    }

    public Event(String eventId, String userId, String created, String status, String avatar, String username) {
        this.eventId = eventId;
        this.userId = userId;
        this.created = created;
        this.status = status;
        this.avatar = avatar;
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
