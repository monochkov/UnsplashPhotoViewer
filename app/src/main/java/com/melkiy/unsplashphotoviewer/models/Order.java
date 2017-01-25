package com.melkiy.unsplashphotoviewer.models;

public enum Order {

    LATEST("latest"), OLDEST("oldest"), POPULAR("popular");

    private final String value;

    Order(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
