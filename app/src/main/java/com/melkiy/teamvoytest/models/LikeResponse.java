package com.melkiy.teamvoytest.models;

public class LikeResponse {

    private Photo photo;
    private User user;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LikeResponse{" +
                "photo=" + photo +
                ", user=" + user +
                '}';
    }
}