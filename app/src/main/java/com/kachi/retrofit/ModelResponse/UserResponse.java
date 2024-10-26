package com.kachi.retrofit.ModelResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {

    String error;
    @SerializedName("user")
    List<User> userList;

    public UserResponse(String error, List<User> userList) {
        this.error = error;
        this.userList = userList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
