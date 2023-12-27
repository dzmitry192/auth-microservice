package com.innowise.authmicroservice.enums;

public enum ActionEnum {
    LOGIN_ACTION("The client has logged into his account"),
    SIGNUP_ACTION("The client has registered an account"),
    REFRESH_ACTION("The client has updated the access token"),
    GET_CLIENTS("The client received a list of clients"),
    GET_CLIENT("The client received the client by his ID"),
    UPDATE_CLIENT("Client data by ID has been updated"),
    DELETE_CLIENT("The client by ID was deleted");

    private String action;

    ActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}
