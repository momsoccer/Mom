package com.mom.soccer.dataDto;

/**
 * Created by sungbo on 2016-08-01.
 */
public class UserRangkinVo {

    private int uid;
    private String username;
    private String profileimgurl;
    private String teamname;
    private String totalscore;
    private int sequence;

    public UserRangkinVo(){};

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(String totalscore) {
        this.totalscore = totalscore;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }


    @Override
    public String toString() {
        return "UserRangkinVo{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", profileimgurl='" + profileimgurl + '\'' +
                ", teamname='" + teamname + '\'' +
                ", totalscore='" + totalscore + '\'' +
                ", sequence=" + sequence +
                '}';
    }
}
