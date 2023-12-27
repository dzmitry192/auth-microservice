package com.innowise.authmicroservice.enums;

import lombok.Data;

public enum ActionTypeEnum {
    LOGIN_ACTION_TYPE("LOGIN"),
    SIGNUP_ACTION_TYPE("SIGNUP"),
    REFRESH_ACTION_TYPE("REFRESH_TOKEN"),
    GET_CLIENTS_TYPE("GET_CLIENTS"),
    GET_CLIENT_TYPE("GET_CLIENT"),
    UPDATE_CLIENT_TYPE("UPDATE_CLIENT"),
    DELETE_CLIENT_TYPE("DELETE_CLIENT");


    private String actionType;

    ActionTypeEnum(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }
}
