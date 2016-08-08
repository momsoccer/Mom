package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-08-08.
 */
public class FollowManage {

    private int followid;
    private int uid;
    private int followuid;
    private String change_date;
    private String attribute1,attribute2,attribute3,attribute4,attribute5;

    public FollowManage(){}

    public FollowManage(int followid, int uid, int followuid, String change_date, String attribute1, String attribute2, String attribute3, String attribute4, String attribute5) {
        this.followid = followid;
        this.uid = uid;
        this.followuid = followuid;
        this.change_date = change_date;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }

    public int getFollowid() {
        return followid;
    }

    public void setFollowid(int followid) {
        this.followid = followid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getFollowuid() {
        return followuid;
    }

    public void setFollowuid(int followuid) {
        this.followuid = followuid;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    @Override
    public String toString() {
        return "FollowManage{" +
                "followid=" + followid +
                ", uid=" + uid +
                ", followuid=" + followuid +
                ", change_date='" + change_date + '\'' +
                ", attribute1='" + attribute1 + '\'' +
                ", attribute2='" + attribute2 + '\'' +
                ", attribute3='" + attribute3 + '\'' +
                ", attribute4='" + attribute4 + '\'' +
                ", attribute5='" + attribute5 + '\'' +
                '}';
    }
}
