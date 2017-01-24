package com.melkiy.teamvoytest.models;

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
