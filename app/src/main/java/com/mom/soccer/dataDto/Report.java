package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-09-11.
 */
public class Report {
    private int id;
    private String type;
    private String reason;
    private int uid;
    private int publisherid;
    private String content;

    //Non DB
    private String username;  //신고한 사람
    private String publisheruser; //신고당한 사람

    public Report(){}

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", uid=" + uid +
                ", publisherid=" + publisherid +
                ", content='" + content + '\'' +
                ", username='" + username + '\'' +
                ", publisheruser='" + publisheruser + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(int publisherid) {
        this.publisherid = publisherid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublisheruser() {
        return publisheruser;
    }

    public void setPublisheruser(String publisheruser) {
        this.publisheruser = publisheruser;
    }
}
