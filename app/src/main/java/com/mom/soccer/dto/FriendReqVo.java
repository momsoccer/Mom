package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-07-12.
 * 친구요청 및 친구 정보 관련 DTO
 */
public class FriendReqVo {

    private String username;
    private String profileimgurl;
    private String requestmessage;
    private String reqdate;

    public FriendReqVo(){}

    public FriendReqVo(String username, String profileimgurl, String requestmessage, String reqdate) {
        this.username = username;
        this.profileimgurl = profileimgurl;
        this.requestmessage = requestmessage;
        this.reqdate = reqdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
    }

    public String getRequestmessage() {
        return requestmessage;
    }

    public void setRequestmessage(String requestmessage) {
        this.requestmessage = requestmessage;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    @Override
    public String toString() {
        return "FriendReqVo{" +
                "username='" + username + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", requestmessage='" + requestmessage + '\'' +
                ", reqdate='" + reqdate + '\'' +
                '}';
    }
}
