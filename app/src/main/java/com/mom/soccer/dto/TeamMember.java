package com.mom.soccer.dto;

/**
 * Created by sungbo on 2016-07-09.
 */
public class TeamMember {

    private int teammemberid;
    private int teamid;
    private int uid;
    private String enabled;
    private String teamname;
    private String username;
    private String authority;
    private String change_creationdate;
    private String change_updatedate;

    public TeamMember(){}

    @Override
    public String toString() {
        return "TeamMember{" +
                "teammemberid=" + teammemberid +
                ", teamid=" + teamid +
                ", uid=" + uid +
                ", enabled='" + enabled + '\'' +
                ", teamname='" + teamname + '\'' +
                ", username='" + username + '\'' +
                ", authority='" + authority + '\'' +
                ", change_creationdate='" + change_creationdate + '\'' +
                ", change_updatedate='" + change_updatedate + '\'' +
                '}';
    }

    public int getTeammemberid() {
        return teammemberid;
    }

    public void setTeammemberid(int teammemberid) {
        this.teammemberid = teammemberid;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getChange_creationdate() {
        return change_creationdate;
    }

    public void setChange_creationdate(String change_creationdate) {
        this.change_creationdate = change_creationdate;
    }

    public String getChange_updatedate() {
        return change_updatedate;
    }

    public void setChange_updatedate(String change_updatedate) {
        this.change_updatedate = change_updatedate;
    }
}
