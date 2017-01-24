package com.melkiy.teamvoytest.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.Instant;

import java.io.Serializable;

public class Photo implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    @JsonProperty("color")
    private String color;

    @JsonProperty("likes")
    private int likes;

    @JsonProperty("liked_by_user")
    private boolean likedByUser;

    @JsonProperty("user")
    private User user;

    @JsonProperty("urls")
    private Urls urls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", width=" + width +
                ", height=" + height +
                ", color='" + color + '\'' +
                ", likes=" + likes +
                ", likedByUser=" + likedByUser +
                ", user=" + user.toString() +
                ", urls=" + urls.toString() +
                '}';
    }
}
