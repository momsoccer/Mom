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

    //use query
    private String username;
    private String teamname;
    private int usermissioncount;
    private int score;
    private int followercount;
    private int followingcount;

    private int mecommentcount;
    private int commentcount;
    private int level;
    private String profileimgurl;
    private int followtype;

    public FollowManage(){}

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
                ", username='" + username + '\'' +
                ", teamname='" + teamname + '\'' +
                ", usermissioncount=" + usermissioncount +
                ", score=" + score +
                ", followercount=" + followercount +
                ", followingcount=" + followingcount +
                ", mecommentcount=" + mecommentcount +
                ", commentcount=" + commentcount +
                ", level=" + level +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", followtype='" + followtype + '\'' +
                '}';
    }



    public int getFollowtype() {
        return followtype;
    }

    public void setFollowtype(int followtype) {
        this.followtype = followtype;
    }

    public String getProfileimgurl() {
        return profileimgurl;
    }

    public void setProfileimgurl(String profileimgurl) {
        this.profileimgurl = profileimgurl;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public int getUsermissioncount() {
        return usermissioncount;
    }

    public void setUsermissioncount(int usermissioncount) {
        this.usermissioncount = usermissioncount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFollowercount() {
        return followercount;
    }

    public void setFollowercount(int followercount) {
        this.followercount = followercount;
    }

    public int getFollowingcount() {
        return followingcount;
    }

    public void setFollowingcount(int followingcount) {
        this.followingcount = followingcount;
    }

    public int getMecommentcount() {
        return mecommentcount;
    }

    public void setMecommentcount(int mecommentcount) {
        this.mecommentcount = mecommentcount;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
