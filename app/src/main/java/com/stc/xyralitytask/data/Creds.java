package com.stc.xyralitytask.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by artem on 1/25/18.
 */

public class Creds {
    @SerializedName("login")
    @Expose
    public String login;

    @SerializedName("password")
    @Expose
    public String password;

    @SerializedName("deviceType")
    @Expose
    public String deviceType;

    @SerializedName("deviceId")
    @Expose
    public String deviceId;

    public Creds(String login, String password, String deviceType, String deviceId) {
        this.login = login;
        this.password = password;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
